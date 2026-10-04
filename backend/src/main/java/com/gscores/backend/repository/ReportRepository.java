package com.gscores.backend.repository;

import com.gscores.backend.repository.projection.ScoreDistributionProjection;
import com.gscores.backend.repository.projection.TopStudentProjection;
import com.gscores.backend.entity.Subject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ReportRepository extends Repository<Subject, Integer> {
    @Query("""
        select
            math.student.registrationNumber as registrationNumber,
            math.score as mathScore,
            physics.score as physicsScore,
            chemistry.score as chemistryScore,
            math.score + physics.score + chemistry.score as totalScore

        from Score math

        join Score physics on physics.student = math.student
        join Score chemistry on chemistry.student = math.student

        where math.subject.code = 'toan'
            and physics.subject.code = 'vat_li'
            and chemistry.subject.code = 'hoa_hoc'
            
        order by (math.score + physics.score + chemistry.score) desc,
            math.student.registrationNumber asc
        """)
    List<TopStudentProjection> findTopGroupAStudents(Pageable pageable);

    @Query("""
        select subject.code as subjectCode,
            subject.name as subjectName,

            sum(case when score.score >= 8 then 1 else 0 end) as atLeast8,

            sum(case when score.score >= 6
                     and score.score < 8
                     then 1 else 0 end) as from6ToUnder8,

            sum(case when score.score >= 4
                     and score.score < 6
                     then 1 else 0 end) as from4ToUnder6,

            sum(case when score.score < 4
                     then 1 else 0 end) as under4
                     
        from Subject subject
        left join Score score on score.subject = subject
        group by subject.id, subject.code, subject.name, subject.displayOrder
        order by subject.displayOrder, subject.id
        """)
    List<ScoreDistributionProjection> findScoreDistribution();
}
