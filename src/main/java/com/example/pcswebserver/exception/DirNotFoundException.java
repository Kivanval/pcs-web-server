package com.example.pcswebserver.exception;

public class DirNotFoundException extends RuntimeException {
    public DirNotFoundException(String message) {
        super(message);
    }

    public DirNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
