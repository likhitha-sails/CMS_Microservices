package com.example.certificate_service.service;

import com.example.certificate_service.entitiy.Certificate;
import com.example.certificate_service.exception.CertificateAlreadyRevokedException;
import com.example.certificate_service.exception.CertificateNotEligibleException;
import com.example.certificate_service.exception.CertificateNotFoundException;
import com.example.certificate_service.exception.CourseServiceException;
import com.example.certificate_service.repository.CertificateRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final CertificateRepository repo;
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    private static final String COURSE_SERVICE_BASE_URL = "http://localhost:8081/api/courses";

    // 1. REST TEMPLATE: Fetch a course by ID
    @CircuitBreaker(name = "COURSE_SERVICE_CB", fallbackMethod = "fallbackCourseDetails")
    public Map<String, Object> getCourseDetails(Long courseId) {
        try {
            log.info("[REST TEMPLATE] Fetching course details for courseId={}", courseId);
            String url = COURSE_SERVICE_BASE_URL + "/" + courseId;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.isEmpty()) {
                throw new CourseServiceException("No course found with ID: " + courseId);
            }

            return response;
        } catch (Exception e) {
            throw new CourseServiceException("Failed to fetch course details: " + e.getMessage());
        }
    }
    public Map<String, Object> fallbackCourseDetails(Long courseId, Throwable t) {
        log.error("Fallback triggered for getCourseDetails({}) due to: {}", courseId, t.getMessage());
        throw new CourseServiceException("Course service is currently unavailable. Please try again later.");
    }

    // 2. WEB CLIENT: Fetch top-rated courses (>4.5)
    @CircuitBreaker(name = "COURSE_SERVICE_CB", fallbackMethod = "fallbackTopRatedCourses")
    public List<Map<String, Object>> getTopRatedCourses() {
        try {
            log.info("[WEB CLIENT] Fetching top-rated courses...");
            WebClient client = webClientBuilder.build();
            List<Map<String, Object>> response = client
                    .get()
                    .uri(COURSE_SERVICE_BASE_URL + "/top-rated")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            if (response == null || response.isEmpty()) {
                throw new CourseServiceException("No top-rated courses found");
            }

            return response;
        } catch (Exception e) {
            throw new CourseServiceException("Error fetching top-rated courses: " + e.getMessage());
        }
    }
    //circuitbreaker fallback
    public List<Map<String, Object>> fallbackTopRatedCourses(Throwable t) {
        log.error("Fallback triggered for getTopRatedCourses due to: {}", t.getMessage());
        return Collections.emptyList();
    }
    // 3. Generate certificate only if progress == 100
    @Transactional
    public Certificate generateCertificate(Long studentId, Long courseId) {
        Double progress;
        try {
            progress = restTemplate.getForObject(
                    COURSE_SERVICE_BASE_URL + "/progress/" + studentId + "/" + courseId,
                    Double.class
            );
        } catch (Exception e) {
            throw new CourseServiceException("Failed to fetch progress from CourseService: " + e.getMessage());
        }
        if (progress == null) {
            throw new CertificateNotEligibleException("Progress data not found for studentId=" + studentId);
        }
        if (progress < 100.0) {
            throw new CertificateNotEligibleException("Certificate not eligible: progress = " + progress + "%");
        }
        Certificate certificate = repo
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElse(new Certificate());
        Map<String, Object> courseDetails = getCourseDetails(courseId);
        certificate.setStudentId(studentId);
        certificate.setCourseId(courseId);
        certificate.setCourseTitle((String) courseDetails.get("title"));
        certificate.setIssuedAt(LocalDateTime.now());
        certificate.setRevoked(false);
        certificate.setCertificateUrl("https://certificates.example.com/" + UUID.randomUUID());
        return repo.save(certificate);
    }

    @Transactional
    public Certificate revokeCertificateByStudentAndCourse(Long studentId, Long courseId) {
        Certificate cert = repo.findByStudentIdAndCourseId(studentId, courseId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new CertificateNotFoundException("Certificate not found for studentId=" + studentId + " and courseId=" + courseId)
                );

        if (cert.isRevoked()) {
            throw new CertificateAlreadyRevokedException("Certificate already revoked for this student and course.");
        }

        cert.setRevoked(true);
        return repo.save(cert);
    }

    @Transactional
    public void deleteCertificateByStudentAndCourse(Long studentId, Long courseId) {
        Certificate cert = repo.findByStudentIdAndCourseId(studentId, courseId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new CertificateNotFoundException("No certificate found to delete for studentId=" + studentId + " and courseId=" + courseId)
                );
        repo.delete(cert);
    }
}
