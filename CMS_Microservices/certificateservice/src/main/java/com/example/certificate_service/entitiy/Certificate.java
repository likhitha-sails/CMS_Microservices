package com.example.certificate_service.entitiy;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue
    private UUID id; // unique certificate ID

    private Long studentId;
    private Long courseId;

    private String courseTitle;
    private String certificateUrl;
    private LocalDateTime issuedAt;

    private boolean revoked = false;
}
