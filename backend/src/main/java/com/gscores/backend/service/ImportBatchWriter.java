package com.gscores.backend.service;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.gscores.backend.dto.ParsedStudentRow;
import com.gscores.backend.entity.Score;
import com.gscores.backend.entity.Student;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.repository.ScoreRepository;
import com.gscores.backend.entity.Subject;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ImportBatchWriter {
    private final EntityManager entityManager;
    private final StudentRepository students;
    private final ScoreRepository scores;

    public ImportBatchWriter(
        EntityManager entityManager, 
        StudentRepository students, 
        ScoreRepository scores) 
    {
        this.entityManager = entityManager;
        this.students = students;
        this.scores = scores;
    }

    @Transactional
    public int write(List<ParsedStudentRow> rows, Map<String, Integer> subjectIds) {
        if (rows.isEmpty()) return 0;
        var numbers = rows.stream().map(ParsedStudentRow::registrationNumber).toList();
        var existing = new HashSet<>(students.findExistingNumbers(numbers));
        var newStudents = new ArrayList<Student>();
        var newScores = new ArrayList<Score>();
        for (var row : rows) {
            if (!existing.add(row.registrationNumber())) continue;
            Student student = new Student(row.registrationNumber(), row.foreignLanguageCode());
            newStudents.add(student);
            for (var entry : row.scores().entrySet()) {
                Integer id = subjectIds.get(entry.getKey());
                if (id == null) throw new IllegalStateException("Missing subject: " + entry.getKey());
                Subject subject = entityManager.getReference(Subject.class, id);
                newScores.add(new Score(student, subject, entry.getValue()));
            }
        }
        students.saveAll(newStudents);
        scores.saveAll(newScores);
        entityManager.flush();
        entityManager.clear();
        return newStudents.size();
    }
}
