package com.zhlzzz.together.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.zhlzzz.together.controllers")
@Slf4j
public class ApiControllerAdvice {

    private RestClientResponseException apiExceptionToRestClientResponseException(ApiException apiException) {
        String responseBody;
        HttpStatus status = apiException.getStatus();
        HttpHeaders headers = apiException.getHeaders();

        try {
            ObjectMapper mapper = new ObjectMapper();
//            Map<String, String> res = new HashMap<>();
//            res.put("error", apiException.getError());
//            res.put("message", apiException.getMessage());
            ApiExceptionResponse res = new ApiExceptionResponse(apiException.getError(), apiException.getMessage());
            responseBody = mapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            log.error("error during build error response", e);
            responseBody = "{\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\"}";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        return new RestClientResponseException(responseBody, status.value(), status.getReasonPhrase(), headers, responseBody.getBytes(), Charset.forName("UTF-8"));
    }

    private ResponseEntity<String> apiExceptionToResponseEntity(ApiException apiException) {
        RestClientResponseException responseException = apiExceptionToRestClientResponseException(apiException);
        return new ResponseEntity<>(responseException.getResponseBodyAsString(), responseException.getResponseHeaders(), HttpStatus.valueOf(responseException.getRawStatusCode()));
    }

//	@ExceptionHandler({ Exception.class })
//	public ResponseEntity<String> handleAnyException(Exception e) {
//		log.error("unknown exception", e);
//		return apiExceptionToResponseEntity(ApiExceptions.unknownException());
//	}

    @ExceptionHandler({ NoHandlerFoundException.class })
    public ResponseEntity<String> handleAnyException(NoHandlerFoundException e) {
        return apiExceptionToResponseEntity(ApiExceptions.notFound("没有这个api。"));
    }

//    @ExceptionHandler({ IllegalArgumentException.class })
//    public ResponseEntity<String> handleClientErrorException(Exception e) {
//        return apiExceptionToResponseEntity(ApiExceptions.badRequest(e.getMessage()));
//    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
        return apiExceptionToResponseEntity(ApiExceptions.badRequest(message));
    }

    @ExceptionHandler({ RestClientResponseException.class })
    public ResponseEntity<String> handleRestException(RestClientResponseException e) {
        return new ResponseEntity<>(e.getResponseBodyAsString(), e.getResponseHeaders(), HttpStatus.valueOf(e.getRawStatusCode()));
    }

    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<String> handleRestException(ApiException e) {
        return apiExceptionToResponseEntity(e);
    }
}
