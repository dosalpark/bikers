package org.example.bikers.domain.mail.repository;

import java.util.Optional;
import org.example.bikers.domain.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {

    Optional<Mail> findMailByEmailEqualsAndAuthCodeEquals(String email, String code);

}
