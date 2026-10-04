package com.gscores.backend.dto;

import java.util.List;

public record StudentScoresResponse(
    String registrationNumber,
    String foreignLanguageCode,
    List<SubjectScoreResponse> scores
) {
}
