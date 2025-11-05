package com.example.CourseService.service;

import com.example.CourseService.entity.*;
import com.example.CourseService.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollRepo;

    // PUT - Update course rating manually
    public Course updateRating(Long courseId, Double rating) {
        log.info("[UPDATE-RATING] Request received → courseId={}, rating={}", courseId, rating);

        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> {
                    log.error("[UPDATE-RATING] Course {} not found!", courseId);
                    return new RuntimeException("Course not found");
                });

        log.debug("[UPDATE-RATING] Found course: {} (current rating={})", c.getTitle(), c.getRating());

        c.setRating(rating);
        Course saved = courseRepo.save(c);

        log.info("[UPDATE-RATING] Rating updated successfully for course '{}'. New rating={}",
                saved.getTitle(), saved.getRating());
        return saved;
    }
    // DELETE - Remove a student from a course
    public void removeEnrollment(Long studentId, Long courseId) {
        log.info("[REMOVE-ENROLLMENT] Request received → studentId={}, courseId={}", studentId, courseId);

        enrollRepo.findAll().stream()
                .filter(e -> {
                    boolean match = e.getStudent().getId().equals(studentId) && e.getCourse().getId().equals(courseId);
                    if (match)
                        log.debug("[REMOVE-ENROLLMENT] Matching enrollment found (enrollmentId={})", e.getId());
                    return match;
                })
                .findFirst()
                .ifPresentOrElse(e -> {
                    enrollRepo.delete(e);
                    log.info("[REMOVE-ENROLLMENT] Student {} successfully removed from course {}", studentId, courseId);
                }, () -> {
                    log.warn("[REMOVE-ENROLLMENT] No enrollment found for student {} in course {}", studentId, courseId);
                });
    }

    //recommendations
    public List<Course> recommendCourses(Long studentId) {
        log.info("[RECOMMEND] Start → fetching recommendations for student {}", studentId);

        // Step 1: Get all course IDs the student is enrolled in
        List<Long> enrolledCourseIds = enrollRepo.findByStudentId(studentId)
                .stream()
                .map(e -> e.getCourse().getId())
                .toList();

        log.debug("[RECOMMEND] Student {} enrolled in: {}", studentId, enrolledCourseIds);

        // Step 2: If no enrolled courses, recommend all with rating > 4.5
        if (enrolledCourseIds.isEmpty()) {
            log.info("[RECOMMEND] Student {} not enrolled anywhere → recommending all top-rated", studentId);
            return courseRepo.findCoursesWithHighRating(4.5);
        }

        // Step 3: Recommend courses not in enrolled list with rating > 4.5
        List<Course> recommendations = courseRepo.findUnenrolledHighRatedCourses(enrolledCourseIds, 4.5);
        log.info("[RECOMMEND] Student {} → {} recommended courses found", studentId, recommendations.size());

        recommendations.forEach(c ->
                log.debug("[RECOMMEND] → id={}, title={}, rating={}", c.getId(), c.getTitle(), c.getRating())
        );

        log.info("[RECOMMEND] End → successfully fetched recommendations for student {}", studentId);
        return recommendations;
    }
}
