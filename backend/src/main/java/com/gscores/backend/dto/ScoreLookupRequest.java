package com.gscores.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ScoreLookupRequest(
    @NotNull(message = "Số báo danh không được để trống.")
    @Pattern(regexp = "[0-9]{1,10}", message = "Số báo danh phải gồm từ 1 đến 10 chữ số (0-9).")
    @DecimalMin(value = "1", message = "Số báo danh phải lớn hơn 0.")
    @DecimalMax(value = "2147483647", message = "Số báo danh không được vượt quá 2147483647.")
    String registrationNumber
) {
    public ScoreLookupRequest {
        registrationNumber = registrationNumber == null ? null : registrationNumber.strip();
    }
}
