package com.dailytempo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "아이디를 입력해 주세요.")
        @Size(min = 4, max = 20, message = "아이디는 4~20자로 입력해 주세요.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "아이디는 영문, 숫자, 밑줄(_)만 사용할 수 있습니다."
        )
        String username,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Size(min = 8, max = 64, message = "비밀번호는 8~64자로 입력해 주세요.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "비밀번호에는 영문과 숫자가 각각 하나 이상 필요합니다."
        )
        String password,

        @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
        String passwordConfirm,

        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(min = 2, max = 30, message = "이름은 2~30자로 입력해 주세요.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z\\s]+$",
                message = "이름에는 한글, 영문, 공백만 사용할 수 있습니다."
        )
        String name,

        @Pattern(
                regexp = "^$|^01[016789]\\d{7,8}$",
                message = "휴대폰 번호는 '-' 없이 10~11자리로 입력해 주세요."
        )
        String phone,

        @Pattern(
                regexp = "^$|^\\d{4}\\.\\d{2}\\.\\d{2}$",
                message = "생년월일은 yyyy.MM.dd 형식으로 입력해 주세요."
        )
        String birthDate
) {
}
