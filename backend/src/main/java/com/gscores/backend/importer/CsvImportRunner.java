package com.gscores.backend.importer;

import com.gscores.backend.service.ImportService;

import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("import")
public class CsvImportRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CsvImportRunner.class);
    private final ImportService service;
    private final ConfigurableApplicationContext context;
    private final String csvPath;
    private final int batchSize;

    public CsvImportRunner(ImportService service, ConfigurableApplicationContext context,
                           @Value("${app.import.csv-path}") String csvPath,
                           @Value("${app.import.batch-size}") int batchSize) {
        this.service = service;
        this.context = context;
        this.csvPath = csvPath;
        this.batchSize = batchSize;
    }

    @Override
    public void run(String... args) throws Exception {
        if (csvPath.isBlank() || !Files.isRegularFile(Path.of(csvPath))) {
            throw new IllegalArgumentException("Set IMPORT_CSV_PATH to an existing CSV file");
        }
        var result = service.importFile(Path.of(csvPath), batchSize);
        if (result.invalidRows() > 0 || result.duplicateSkipped() > 0) {
            log.warn("Import completed with skipped rows; valid batches committed: {}", result);
        } else {
            log.info("Import completed: {}", result);
        }
        context.close();
    }
}
