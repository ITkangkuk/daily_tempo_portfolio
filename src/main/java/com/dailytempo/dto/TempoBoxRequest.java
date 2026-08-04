package com.dailytempo.dto;

import com.dailytempo.domain.TempoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TempoBoxRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(max = 20, message = "이름은 20자 이하로 입력해 주세요.")
        String name,

        @NotNull(message = "템포 타입을 선택해 주세요.")
        TempoType type,

        @NotBlank(message = "템포 색상을 선택해 주세요.")
        @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "올바른 색상 값을 선택해 주세요.")
        String color
) {
}
