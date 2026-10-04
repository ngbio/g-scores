package com.gscores.backend.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String registrationNumber) {
        super("Không tìm thấy thí sinh với số báo danh đã nhập: " + registrationNumber);
    }
}
