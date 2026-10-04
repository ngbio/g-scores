package com.gscores.backend.repository;

import com.gscores.backend.entity.Score;
import com.gscores.backend.entity.ScoreId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {
    @Query("select s from Score s where s.id.registrationNumber = :registrationNumber")
    List<Score> findByRegistrationNumber(@Param("registrationNumber") Integer registrationNumber);
}
