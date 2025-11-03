package com.example.certificate_service.exception;
public class CertificateNotEligibleException extends RuntimeException {
    public CertificateNotEligibleException(String message) {
        super(message);
    }
}
