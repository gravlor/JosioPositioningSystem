package com.gravlor.josiopositioningsystem.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ApiError {

    private final HttpStatus status;
    private final List<String> messages;

    public ApiError(HttpStatus status, String message) {
        this(status, Collections.singletonList(message));
    }

    public ApiError(HttpStatus status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }

}
