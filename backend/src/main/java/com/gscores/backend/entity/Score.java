package com.gscores.backend.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "scores", indexes = {
    @Index(name = "idx_scores_subject_score", columnList = "subject_id,score")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score implements Persistable<ScoreId> {
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markPersisted() { isNew = false; }

    @EmbeddedId
    private ScoreId id;

    @MapsId("registrationNumber")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_registration_number", nullable = false)
    private Student student;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("10.00")
    @Digits(integer = 2, fraction = 2)
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal score;

    public Score(Student student, Subject subject, BigDecimal score) {
        this.id = new ScoreId(student.getRegistrationNumber(), subject.getId());
        this.student = student;
        this.subject = subject;
        this.score = score;
    }
}
