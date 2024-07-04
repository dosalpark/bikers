package org.example.bikers.domain.mail.dto;

import lombok.Builder;

@Builder
public class MailSaveVerificationCodeAndTypeRequestDto {

    private String verificationCode;
    private String type;

}
