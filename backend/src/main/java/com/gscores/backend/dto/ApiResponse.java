package com.gscores.backend.dto;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
}