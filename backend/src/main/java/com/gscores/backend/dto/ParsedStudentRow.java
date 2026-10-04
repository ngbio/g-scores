package com.gscores.backend.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ParsedStudentRow(
    Integer registrationNumber,
    String foreignLanguageCode,
    Map<String, BigDecimal> scores
) {

    public ParsedStudentRow {
        scores = Map.copyOf(scores);
    }
}
