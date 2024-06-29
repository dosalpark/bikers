package org.example.bikers.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.mail.entity.Mail;
import org.example.bikers.domain.mail.repository.MailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;

    @Value("${mail.check-email.subject}")
    private String subject;
    @Value("${mail.check-email.content}")
    private String content;
    @Value("${mail.check-email.auth-code.expiration-second}")
    private long expireSeconds;

    public void send(String email) throws MessagingException {
        String code = createCode();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(content + code, true);
        mailSender.send(message);

        Mail newMail = new Mail(email, code, expireSeconds);
        mailRepository.save(newMail);
    }

    private String createCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }
}
