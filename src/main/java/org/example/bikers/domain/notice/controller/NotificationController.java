package org.example.bikers.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.notice.service.NotificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;



}
