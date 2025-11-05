package com.example.certificate_service.controller;

import com.example.certificate_service.entitiy.Certificate;
import com.example.certificate_service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // GET - fetch course details (RestTemplate)
    @GetMapping("/course/{courseId}")
    public Map<String, Object> getCourseDetails(@PathVariable Long courseId) {
        return certificateService.getCourseDetails(courseId);
    }

    // GET - fetch top-rated courses (WebClient)
    @GetMapping("/top-rated")
    public List<Map<String, Object>> getTopRatedCourses() {
        return certificateService.getTopRatedCourses();
    }

    // POST - generate certificate (combined call)
    @PostMapping("/generate/{studentId}/{courseId}")
    public Certificate generateCertificate(@PathVariable Long studentId, @PathVariable Long courseId) {
        return certificateService.generateCertificate(studentId, courseId);
    }
    @PutMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestParam Long studentId,
                                               @RequestParam Long courseId) {
        return ResponseEntity.ok(certificateService.revokeCertificateByStudentAndCourse(studentId, courseId));
    }

    // DELETE – delete certificate by studentId & courseId
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCertificate(@RequestParam Long studentId,
                                               @RequestParam Long courseId) {
        certificateService.deleteCertificateByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok("Certificate deleted successfully for studentId=" +
                studentId + " and courseId=" + courseId);
    }
}
