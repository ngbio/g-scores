package com.gscores.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import org.springframework.data.domain.Persistable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student implements Persistable<String> {
    @Transient
    private boolean isNew = true;

    @Override
    public String getId() { return registrationNumber; }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markPersisted() { isNew = false; }

    @Id
    @Column(name = "registration_number", length = 255, nullable = false)
    private String registrationNumber;

    @Column(name = "foreign_language_code", length = 255)
    private String foreignLanguageCode;

    public Student(String registrationNumber, String foreignLanguageCode) {
        this.registrationNumber = registrationNumber;
        this.foreignLanguageCode = foreignLanguageCode;
    }
}
