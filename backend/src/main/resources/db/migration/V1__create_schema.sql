CREATE TABLE students (
    registration_number VARCHAR(255) NOT NULL PRIMARY KEY,
    foreign_language_code VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE subjects (
    id INT NOT NULL PRIMARY KEY,
    code VARCHAR(32) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_order INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE scores (
    student_registration_number VARCHAR(255) NOT NULL,
    subject_id INT NOT NULL,
    score DECIMAL(4,2) NOT NULL,
    PRIMARY KEY (student_registration_number, subject_id),
    CONSTRAINT fk_scores_student FOREIGN KEY (student_registration_number)
        REFERENCES students (registration_number),
    CONSTRAINT fk_scores_subject FOREIGN KEY (subject_id) REFERENCES subjects (id),
    CONSTRAINT chk_score_range CHECK (score >= 0 AND score <= 10),
    INDEX idx_scores_subject_score (subject_id, score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
