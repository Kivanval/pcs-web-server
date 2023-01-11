package com.example.pcswebserver.exception;

public class StoreDirNotFoundException extends RuntimeException {
    public StoreDirNotFoundException(String message) {
        super(message);
    }

    public StoreDirNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
