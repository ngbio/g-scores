package com.gscores.backend.dto;

public record ScoreDistributionResponse(
        String subjectCode,
        String subjectName,
        long atLeast8,
        long from6ToUnder8,
        long from4ToUnder6,
        long under4
) {
}
