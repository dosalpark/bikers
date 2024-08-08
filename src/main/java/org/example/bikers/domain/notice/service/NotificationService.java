package org.example.bikers.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.notice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;



}
