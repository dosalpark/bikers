package org.example.bikers.domain.mail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MailGetEmailRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "이메일 주소 형식을 지켜주세요")
    private String email;

}
