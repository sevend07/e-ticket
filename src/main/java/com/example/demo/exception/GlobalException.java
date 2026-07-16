package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.DTO.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response.ErrorResponse> handleBusiness(BusinessException x) {
        HttpStatus status = Objects.requireNonNull(x.getStatus());
        return ResponseEntity.status(status)
                .body(Response.ErrorResponse.of(x.getMessage(), status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response.ErrorResponse> handleGeneral(Exception x) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(Response.ErrorResponse.of(x.getMessage(), status));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response.ErrorResponse> handleValidation(MethodArgumentNotValidException x) {
        Map<String, String> errors = new HashMap<>();
        x.getBindingResult().getAllErrors().forEach(e -> {
            String field = ((FieldError) e).getField();
            errors.put(field, e.getDefaultMessage());
        });

        return ResponseEntity.badRequest()
                .body(Response.ErrorResponse.builder()
                        .success(false)
                        .status(400)
                        .error("Validation Failed")
                        .message("Check Validation Error")
                        .validationErrors(errors)
                        .build());
    }
}
