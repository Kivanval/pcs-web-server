package com.example.pcswebserver.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("User with username %s already exists" .formatted(username));
    }
}
