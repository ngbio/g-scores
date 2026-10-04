package com.gscores.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Locale;

public record TopStudentsRequest(
        @NotNull(message = "Khối thi không được để trống.")
        @Pattern(regexp = "A", 
                message = "Chỉ hỗ trợ khối A (Toán, Vật lí, Hóa học).")
                String group) {
    public TopStudentsRequest {
        group = group == null ? null : group.strip().toUpperCase(Locale.ROOT);
    }
}
