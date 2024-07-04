package org.example.bikers.domain.mail.service;

import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.mail.dto.MailGetVerificationCodeAndTypeResponseDto;
import org.example.bikers.domain.mail.dto.MailSaveVerificationCodeAndTypeRequestDto;
import org.example.bikers.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    private static final String TYPE_OF_SIGNUP = "signup";
    private static final String TYPE_OF_PASSWORD_FORGET = "password-forget";

    @Value("${mail.check-email.subject}")
    private String subject;
    @Value("${mail.check-email.content}")
    private String content;
    @Value("${mail.check-email.auth-code.expiration-second}")
    private long expireSeconds;

    @Transactional
    public void sendVerificationCodeForSignup(String email) throws MessagingException {
        memberService.validateByDuplicateEmail(email);
        String verificationCode = createVerificationCode();

        MailSaveVerificationCodeAndTypeRequestDto requestDto = MailSaveVerificationCodeAndTypeRequestDto.builder()
            .verificationCode(verificationCode)
            .type(TYPE_OF_SIGNUP)
            .build();

        String jsonString = gson.toJson(requestDto,
            MailSaveVerificationCodeAndTypeRequestDto.class);

        redisTemplate.opsForValue().set(email, jsonString, expireSeconds, TimeUnit.SECONDS);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + verificationCode, true);
        mailSender.send(message);
    }

    @Transactional
    public void verifyVerificationCodeForSignup(String email, String code) {
        String savedCodeAndType = redisTemplate.opsForValue().get(email);
        if (!StringUtils.hasText(savedCodeAndType)) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        }

        MailGetVerificationCodeAndTypeResponseDto responseDto = gson.fromJson(savedCodeAndType,
            MailGetVerificationCodeAndTypeResponseDto.class);

        if (!TYPE_OF_SIGNUP.equals(responseDto.getType())) {
            throw new IllegalArgumentException("비정상적인 접근입니다");
        }
        if (!code.equals(responseDto.getVerificationCode())) {
            throw new DuplicateKeyException("인증번호가 일치하지 않습니다.");
        }

        redisTemplate.delete(email);
    }

    @Transactional
    public void sendVerificationCodeForPasswordForget(String email) throws MessagingException {
        memberService.validateByLocalUser(email);
        String verificationCode = createVerificationCode();

        MailSaveVerificationCodeAndTypeRequestDto requestDto = MailSaveVerificationCodeAndTypeRequestDto.builder()
            .verificationCode(verificationCode)
            .type(TYPE_OF_PASSWORD_FORGET)
            .build();

        String jsonString = gson.toJson(requestDto,
            MailSaveVerificationCodeAndTypeRequestDto.class);

        redisTemplate.opsForValue().set(email, jsonString, expireSeconds, TimeUnit.SECONDS);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + verificationCode, true);
        mailSender.send(message);
    }

    public void verifyVerificationCodeForPasswordForget(String email, String code) {
        String savedCodeAndType = redisTemplate.opsForValue().get(email);
        if (!StringUtils.hasText(savedCodeAndType)) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        }

        MailGetVerificationCodeAndTypeResponseDto responseDto = gson.fromJson(savedCodeAndType,
            MailGetVerificationCodeAndTypeResponseDto.class);

        if (!TYPE_OF_PASSWORD_FORGET.equals(responseDto.getType())) {
            throw new IllegalArgumentException("비정상적인 접근입니다");
        }
        if (!code.equals(responseDto.getVerificationCode())) {
            throw new DuplicateKeyException("인증번호가 일치하지 않습니다.");
        }

        redisTemplate.delete(email);
    }

    private String createVerificationCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }

}
