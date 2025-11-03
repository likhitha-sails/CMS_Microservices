package com.example.certificate_service.repository;

import com.example.certificate_service.entitiy.Certificate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Optional<Certificate> findByStudentIdAndCourseId(Long studentId, Long courseId);
}


