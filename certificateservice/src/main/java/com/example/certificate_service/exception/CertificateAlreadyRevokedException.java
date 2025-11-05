package com.example.certificate_service.exception;

public class CertificateAlreadyRevokedException extends RuntimeException {
    public CertificateAlreadyRevokedException(String message) {
        super(message);
    }
}
