package com.example.CourseService.repository;

import com.example.CourseService.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e.progress FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    Double getCourseProgress(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    List<Enrollment> findByStudentId(Long studentId);
}
