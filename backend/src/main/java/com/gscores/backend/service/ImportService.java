package com.gscores.backend.service;

import java.io.IOException;
import java.io.PushbackReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.gscores.backend.dto.ParsedStudentRow;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.repository.SubjectRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
    private static final Logger log = LoggerFactory.getLogger(ImportService.class);
    static final List<String> SUBJECT_CODES = List.of(
        "toan", "ngu_van", "ngoai_ngu", "vat_li", "hoa_hoc",
        "sinh_hoc", "lich_su", "dia_li", "gdcd");
    static final List<String> HEADERS = List.of(
        "sbd", "toan", "ngu_van", "ngoai_ngu", "vat_li", "hoa_hoc",
        "sinh_hoc", "lich_su", "dia_li", "gdcd", "ma_ngoai_ngu");

    private final ImportBatchWriter writer;
    private final SubjectRepository subjects;

    public record Result(long rowsRead, long inserted, long existingSkipped,
                         long duplicateSkipped, long invalidRows, long elapsedMillis) {}

    public ImportService(ImportBatchWriter writer, SubjectRepository subjects) {
        this.writer = writer;
        this.subjects = subjects;
    }

    public Result importFile(Path path, int batchSize) throws IOException {
        if (batchSize < 1 || batchSize > 5000) {
            throw new IllegalArgumentException("Batch size must be between 1 and 5000");
        }
        long started = System.nanoTime();
        long read = 0, inserted = 0, existing = 0, duplicates = 0, invalid = 0;
        var subjectIds = subjects.findAll().stream()
            .collect(Collectors.toMap(Subject::getCode, Subject::getId));
        if (!subjectIds.keySet().containsAll(SUBJECT_CODES)) {
            throw new IllegalStateException("Missing subjects; run Flyway migrations first");
        }

        var seenNumbers = new HashSet<Integer>();
        var batch = new ArrayList<ParsedStudentRow>(batchSize);
        try (var parser = openCsv(path)) {
            for (var record : parser) {
                read++;
                try {
                    var row = parseRow(record);
                    if (seenNumbers.add(row.registrationNumber())) batch.add(row);
                    else duplicates++;
                } catch (IllegalArgumentException ex) {
                    invalid++;
                    if (invalid <= 20) {
                        log.warn("Invalid CSV record {}: {}", record.getRecordNumber(), ex.getMessage());
                    }
                }
                if (batch.size() >= batchSize) {
                    log.info("Writing batch: rowsRead={}, batchSize={}", read, batch.size());
                    int added = writer.write(batch, subjectIds);
                    inserted += added;
                    existing += batch.size() - added;
                    log.info("Batch committed: added={}, totalInserted={}", added, inserted);
                    batch.clear();
                }
                if (read % 10000 == 0) {
                    log.info("Import progress: read={}, inserted={}, existing={}", read, inserted, existing);
                }
            }
            if (!batch.isEmpty()) {
                log.info("Writing final batch: rowsRead={}, batchSize={}", read, batch.size());
                int added = writer.write(batch, subjectIds);
                inserted += added;
                existing += batch.size() - added;
                log.info("Batch committed: added={}, totalInserted={}", added, inserted);
            }
        } catch (IOException | RuntimeException ex) {
            log.error("Import stopped; committed batches are retained. "
                    + "rowsRead={}, inserted={}, existingSkipped={}, duplicateSkipped={}, invalidRows={}",
                read, inserted, existing, duplicates, invalid, ex);
            throw ex;
        }
        return new Result(read, inserted, existing, duplicates, invalid,
            (System.nanoTime() - started) / 1_000_000);
    }

    static CSVParser openCsv(Path path) throws IOException {
        var reader = new PushbackReader(Files.newBufferedReader(path, StandardCharsets.UTF_8), 1);
        try {
            int first = reader.read();
            if (first != -1 && first != '\uFEFF') reader.unread(first);
            var parser = CSVFormat.DEFAULT.builder()
                .setHeader().setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(false)
                .setDuplicateHeaderMode(DuplicateHeaderMode.DISALLOW)
                .get().parse(reader);
            if (parser.getHeaderNames().size() != HEADERS.size()
                    || !new HashSet<>(parser.getHeaderNames()).equals(new HashSet<>(HEADERS))) {
                parser.close();
                throw new IllegalArgumentException("CSV must contain exactly these headers: " + HEADERS);
            }
            return parser;
        } catch (IOException | RuntimeException ex) {
            reader.close();
            throw ex;
        }
    }

    static ParsedStudentRow parseRow(CSVRecord record) {
        if (!record.isConsistent()) {
            throw new IllegalArgumentException("Column count does not match header");
        }
        String rawSbd = record.get("sbd").trim();
        if (!rawSbd.matches("[0-9]{1,10}")) {
            throw new IllegalArgumentException("SBD must contain 1 to 10 digits");
        }
        Integer sbd = Integer.valueOf(rawSbd);
        if (sbd <= 0) {
            throw new IllegalArgumentException("SBD must be a positive integer");
        }
        String language = record.get("ma_ngoai_ngu").trim();
        var scores = new LinkedHashMap<String, BigDecimal>();
        for (String code : SUBJECT_CODES) {
            String raw = record.get(code).trim();
            if (raw.isEmpty()) continue;
            if (!raw.matches("[0-9]{1,2}(\\.[0-9]{1,2})?")) {
                throw new IllegalArgumentException(code + " must be a non-negative number with at most 2 fraction digits");
            }
            BigDecimal score = new BigDecimal(raw);
            if (score.compareTo(BigDecimal.TEN) > 0) {
                throw new IllegalArgumentException(code + " must be between 0 and 10");
            }
            scores.put(code, score);
        }
        return new ParsedStudentRow(sbd, language.isEmpty() ? null : language, scores);
    }
}
