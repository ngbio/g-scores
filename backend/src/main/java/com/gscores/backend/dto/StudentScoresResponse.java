package com.gscores.backend.dto;

import java.util.List;

public record StudentScoresResponse(
    Integer registrationNumber,
    String foreignLanguageCode,
    List<SubjectScoreResponse> scores
) {
}
