package com.example.CourseService.repository;

import com.example.CourseService.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {
    // Fetch courses not enrolled in by the user & with rating > threshold
    @Query("""
        SELECT c FROM Course c WHERE c.id NOT IN :enrolledCourseIds AND c.rating> :minRating ORDER BY c.rating DESC
    """)
    List<Course> findUnenrolledHighRatedCourses(List<Long> enrolledCourseIds, Double minRating);

    // Fallback if student hasn’t enrolled anywhere
    @Query("""
SELECT c FROM Course c
        WHERE c.rating > :minRating
        ORDER BY c.rating DESC
    """)
    List<Course> findCoursesWithHighRating(Double minRating);
}
