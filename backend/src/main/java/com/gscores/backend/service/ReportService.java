package com.gscores.backend.service;

import com.gscores.backend.dto.ScoreDistributionResponse;
import com.gscores.backend.dto.TopStudentResponse;
import org.springframework.data.domain.PageRequest;
import com.gscores.backend.repository.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reports;

    @Transactional(readOnly = true)
    public List<TopStudentResponse> getTopGroupAStudents() {
        return reports.findTopGroupAStudents(PageRequest.of(0, 10)).stream()
                .map(row -> new TopStudentResponse(
                        row.getRegistrationNumber(),
                        row.getMathScore(),
                        row.getPhysicsScore(),
                        row.getChemistryScore(),
                        row.getTotalScore()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScoreDistributionResponse> getScoreDistribution() {
        return reports.findScoreDistribution().stream()
                .map(row -> new ScoreDistributionResponse(
                        row.getSubjectCode(),
                        row.getSubjectName(),
                        row.getAtLeast8(),
                        row.getFrom6ToUnder8(),
                        row.getFrom4ToUnder6(),
                        row.getUnder4()))
                .toList();
    }
}
