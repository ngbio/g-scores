package com.gscores.backend.dto;

import java.math.BigDecimal;

public record SubjectScoreResponse(
    String subjectCode, 
    String subjectName, 
    BigDecimal score
) {
}
