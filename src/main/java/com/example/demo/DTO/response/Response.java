package com.example.demo.DTO.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

public class Response {
    @Data
    @Builder
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BaseResponse<T> {
        private Boolean success;
        private String message;
        private T data;
        private PaginationMetadata paginationMetadata;

    }

    public static <T> BaseResponse<T> generateResponse(
            String message,
            T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> generateResponse(
            String message,
            T data,
            PaginationMetadata pagination) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .paginationMetadata(pagination)
                .build();
    }

    @Data
    @Builder
    public static class PaginationMetadata {
        private Integer page, per_page,
                total_items, total_pages;
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private Boolean success;
        private Integer status;
        private String error;
        private String message;
        private Map<String, String> validationErrors;

        public static ErrorResponse of(String message, HttpStatus status) {
            return ErrorResponse.builder()
                    .success(false)
                    .status(status.value())
                    .error(status.getReasonPhrase())
                    .message(message)
                    .build();
        }
    }
}
