package com.gscores.backend.controller;

import com.gscores.backend.dto.ApiResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        return new ApiResponse<>(200, "OK", Map.of("status", "UP"));
    }
}
