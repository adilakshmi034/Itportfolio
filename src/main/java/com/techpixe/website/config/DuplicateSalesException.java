package com.techpixe.website.config;

public class DuplicateSalesException extends RuntimeException {
    public DuplicateSalesException(String message) {
        super(message);
    }
}