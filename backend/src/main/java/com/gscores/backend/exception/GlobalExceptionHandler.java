package com.gscores.backend.exception;

import com.gscores.backend.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            ConstraintViolationException exception) {

        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Dữ liệu không hợp lệ");

        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStudentNotFound(
            StudentNotFoundException exception) {

        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var httpStatus = HttpStatus.resolve(status.value());
        String message = httpStatus == null ? "Yêu cầu không hợp lệ." : httpStatus.getReasonPhrase();
        return super.handleExceptionInternal(exception,
                new ApiResponse<>(status.value(), message, null), headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception exception) {

        LOGGER.error("Unexpected API error", exception);

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Hệ thống gặp lỗi khi xử lý yêu cầu."
        );
    }

    private ResponseEntity<ApiResponse<Void>> error(
            HttpStatus status,
            String message) {

        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                        status.value(),
                        message,
                        null
                ));
    }
}
