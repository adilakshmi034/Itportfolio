package com.techpixe.website.config;

public class InvalidMobileNumberException extends RuntimeException {
    public InvalidMobileNumberException(String message) {
        super(message);
    }
}