package org.example.bikers.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
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

        redisTemplate.opsForValue().set(email, verificationCode, expireSeconds, TimeUnit.SECONDS);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + verificationCode, true);
        mailSender.send(message);
    }

    @Transactional
    public void sendVerificationCodeForPasswordForget(String email) throws MessagingException {
        memberService.validateByLocalUser(email);
        String verificationCode = createVerificationCode();

        redisTemplate.opsForValue().set(email, verificationCode, expireSeconds, TimeUnit.SECONDS);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + verificationCode, true);
        mailSender.send(message);
    }

    public void verify(String email, String code) {
        String saveCode = redisTemplate.opsForValue().get(email);
        if (!StringUtils.hasText(saveCode)) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        }
        if (!code.equals(saveCode)) {
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
