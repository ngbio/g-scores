package com.gscores.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ScoreLookupRequest(
    @NotNull(message = "Số báo danh không được để trống.")
    @Pattern(regexp = "[0-9]{8,}", message = "Số báo danh phải gồm ít nhất 8 chữ số (0-9).")
    @Size(max = 255, message = "Số báo danh không được vượt quá 255 ký tự.")
    String registrationNumber
) {
    public ScoreLookupRequest {
        registrationNumber = registrationNumber == null ? null : registrationNumber.strip();
    }
}
