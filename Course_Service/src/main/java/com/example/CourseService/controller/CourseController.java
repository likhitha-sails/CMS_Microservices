package com.example.CourseService.controller;

import com.example.CourseService.entity.Course;
import com.example.CourseService.repository.CourseRepository;
import com.example.CourseService.repository.EnrollmentRepository;
import com.example.CourseService.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @PutMapping("/{courseId}/rating")
    public Course updateRating(@PathVariable Long courseId,@Valid @RequestParam Double rating) {
        return service.updateRating(courseId, rating);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public void removeEnrollment(@PathVariable Long studentId, @PathVariable Long courseId) {
        service.removeEnrollment(studentId, courseId);
    }

    @GetMapping("/recommend/{studentId}")
    public List<Course> getRecommendations(@PathVariable Long studentId) {
        return service.recommendCourses(studentId);
    }
    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    @GetMapping("/top-rated")
    public List<Course> getTopRatedCourses() {
        return courseRepository.findAll()
                .stream()
                .filter(c -> c.getRating() > 4.5)
                .collect(Collectors.toList());
    }
    @GetMapping("/progress/{studentId}/{courseId}")
    public Double getCourseProgress(@PathVariable Long studentId, @PathVariable Long courseId) {
        return enrollmentRepository.getCourseProgress(studentId, courseId);
    }
}
