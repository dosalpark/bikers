package org.example.bikers.domain.mail.dto;

import lombok.Getter;

@Getter
public class MailGetVerificationCodeAndTypeResponseDto {

    private String verificationCode;
    private String type;

}
