package com.gscores.backend.controller;

import com.gscores.backend.dto.ApiResponse;
import com.gscores.backend.dto.ScoreDistributionResponse;
import com.gscores.backend.dto.TopStudentResponse;
import com.gscores.backend.dto.TopStudentsRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import com.gscores.backend.service.ReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final Validator validator;

    @GetMapping("/top-students")
    public ApiResponse<List<TopStudentResponse>> getTopStudents(
            @RequestParam("group") String group) {

        var request = new TopStudentsRequest(group);
        var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        
        return new ApiResponse<>(200, "Lấy top 10 thí sinh khối A thành công",
                reportService.getTopGroupAStudents());
    }

    @GetMapping("/score-distribution")
    public ApiResponse<List<ScoreDistributionResponse>> getScoreDistribution() {
        return new ApiResponse<>(200, "Thống kê điểm theo môn thành công",
                reportService.getScoreDistribution());
    }
}
