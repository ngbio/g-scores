package com.gscores.backend.service;

import com.gscores.backend.exception.StudentNotFoundException;
import com.gscores.backend.dto.StudentScoresResponse;
import com.gscores.backend.dto.SubjectScoreResponse;
import com.gscores.backend.repository.ScoreRepository;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.repository.SubjectRepository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository students;
    private final ScoreRepository scores;
    private final SubjectRepository subjects;

    @Transactional(readOnly = true)
    public StudentScoresResponse getScores(Integer registrationNumber) {
        var student = students.findById(registrationNumber)
            .orElseThrow(() ->
                new StudentNotFoundException(registrationNumber));

        Map<Integer, BigDecimal> scoreBySubject = new HashMap<>();
        for (var score : scores.findByRegistrationNumber(registrationNumber)) {
            scoreBySubject.put(score.getId().getSubjectId(), score.getScore());
        }

        var subjectScores = subjects.findAllByOrderByDisplayOrderAscIdAsc().stream()
            .map(subject -> new SubjectScoreResponse(subject.getCode(), subject.getName(),
                scoreBySubject.get(subject.getId())))
            .toList();

        return new StudentScoresResponse(student.getRegistrationNumber(),
            student.getForeignLanguageCode(), subjectScores);
    }
}
