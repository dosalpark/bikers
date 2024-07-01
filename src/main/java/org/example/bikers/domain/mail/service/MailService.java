package org.example.bikers.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.mail.entity.Mail;
import org.example.bikers.domain.mail.repository.MailRepository;
import org.example.bikers.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final MemberService memberService;

    @Value("${mail.check-email.subject}")
    private String subject;
    @Value("${mail.check-email.content}")
    private String content;
    @Value("${mail.check-email.auth-code.expiration-second}")
    private long expireSeconds;

    @Transactional
    public void sendVerificationCodeForSignup(String email) throws MessagingException {
        String verificationCode = createVerificationCode();

        Mail newMail = new Mail(email, verificationCode, expireSeconds);
        mailRepository.save(newMail);

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

        Mail newMail = new Mail(email, verificationCode, expireSeconds);
        mailRepository.save(newMail);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + verificationCode, true);
        mailSender.send(message);
    }

    public void verify(String email, String code) {
        Mail getMail = mailRepository.findMailByEmailEqualsAndAuthCodeEquals(email, code)
            .orElse(null);
        if (getMail == null) {
            throw new DuplicateKeyException("인증번호가 일치하지 않습니다.");
        }
        if (LocalDateTime.now().isAfter(getMail.getExpireDate())) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        }
        mailRepository.delete(getMail);
    }

    private String createVerificationCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }

}
