package com.gscores.backend.controller;

import com.gscores.backend.dto.ApiResponse;
import com.gscores.backend.dto.ScoreLookupRequest;
import com.gscores.backend.dto.StudentScoresResponse;
import com.gscores.backend.service.StudentService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final Validator validator;

    @GetMapping("/{registrationNumber}/scores")
    public ApiResponse<StudentScoresResponse> getScores(
        @PathVariable("registrationNumber") String registrationNumber) {
            
        var request = new ScoreLookupRequest(registrationNumber);
        var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        var studentScores =
                studentService.getScores(request.registrationNumber());

        return new ApiResponse<>(200, "Lấy điểm sinh viên thành công", studentScores);
    }
}
