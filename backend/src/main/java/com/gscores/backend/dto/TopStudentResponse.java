package com.gscores.backend.dto;

import java.math.BigDecimal;

public record TopStudentResponse(
        Integer registrationNumber,
        BigDecimal mathScore,
        BigDecimal physicsScore,
        BigDecimal chemistryScore,
        BigDecimal totalScore) {
}
