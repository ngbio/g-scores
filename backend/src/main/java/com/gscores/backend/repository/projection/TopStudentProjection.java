package com.gscores.backend.repository.projection;

import java.math.BigDecimal;

public interface TopStudentProjection {
    Integer getRegistrationNumber();
    BigDecimal getMathScore();
    BigDecimal getPhysicsScore();
    BigDecimal getChemistryScore();
    BigDecimal getTotalScore();
}
