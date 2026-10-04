package com.gscores.backend.repository.projection;

public interface ScoreDistributionProjection {
    String getSubjectCode();
    String getSubjectName();
    Long getAtLeast8();
    Long getFrom6ToUnder8();
    Long getFrom4ToUnder6();
    Long getUnder4();
}
