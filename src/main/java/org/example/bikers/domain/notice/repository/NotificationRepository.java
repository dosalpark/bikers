package org.example.bikers.domain.notice.repository;

import org.example.bikers.domain.notice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
