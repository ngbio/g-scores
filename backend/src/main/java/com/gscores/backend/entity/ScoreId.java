package com.gscores.backend.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "student_registration_number")
    private Integer registrationNumber;

    @Column(name = "subject_id")
    private Integer subjectId;

    public ScoreId(Integer registrationNumber, Integer subjectId) {
        this.registrationNumber = registrationNumber;
        this.subjectId = subjectId;
    }
}
