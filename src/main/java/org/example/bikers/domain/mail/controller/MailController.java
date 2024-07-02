package org.example.bikers.domain.mail.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.mail.dto.MailGetEmailRequestDto;
import org.example.bikers.domain.mail.dto.MailGetVerifyEmailRequestDto;
import org.example.bikers.domain.mail.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/signup/verification-code")
    public ResponseEntity<Void> sendVerificationCodeForSignup(
        @Valid @RequestBody MailGetEmailRequestDto requestDto)
        throws MessagingException {
        mailService.sendVerificationCodeForSignup(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/forget-password/verification-code")
    public ResponseEntity<Void> sendVerificationCodeForPasswordForget(
        @Valid @RequestBody MailGetEmailRequestDto requestDto)
        throws MessagingException {
        mailService.sendVerificationCodeForPasswordForget(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> verify(
        @Valid @RequestBody MailGetVerifyEmailRequestDto requestDto) {
        mailService.verify(
            requestDto.getEmail(),
            requestDto.getCode());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
