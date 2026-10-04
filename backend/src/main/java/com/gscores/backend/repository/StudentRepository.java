package com.gscores.backend.repository;

import com.gscores.backend.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("select s.registrationNumber from Student s where s.registrationNumber in :numbers")
    List<String> findExistingNumbers(@Param("numbers") List<String> numbers);
}
