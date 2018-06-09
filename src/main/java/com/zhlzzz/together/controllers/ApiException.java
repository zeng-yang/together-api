package com.zhlzzz.together.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ApiException extends RuntimeException {
    @Getter
    @Setter
    private String error;
    @Getter @Setter
    private String message;
    @Getter @Setter @NonNull
    private HttpStatus status;
    @Getter @Setter @NonNull
    private HttpHeaders headers;

    public ApiException(String error, String message, @Nullable HttpStatus status, @Nullable HttpHeaders headers) {
        this.error = error;
        this.message = message;
        this.status = status == null ? HttpStatus.BAD_REQUEST : status;
        this.headers = headers == null ? new HttpHeaders() : headers;
    }

    public ApiException(String error, String message, @Nullable HttpStatus status) {
        this(error, message, status, null);
    }

    public ApiException(String error, String message) {
        this(error, message, null, null);
    }

}
