package com.example.pcswebserver.exception;

public class StoreDirectoryNotFoundException extends RuntimeException {
    public StoreDirectoryNotFoundException(String message) {
        super(message);
    }

    public StoreDirectoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
