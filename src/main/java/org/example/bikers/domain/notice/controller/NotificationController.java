package org.example.bikers.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.notice.dto.NotificationGetResponseDto;
import org.example.bikers.domain.notice.service.NotificationService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<CommonResponseDto<Slice<NotificationGetResponseDto>>> getNotifications(
        @PageableDefault Pageable pageable,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Slice<NotificationGetResponseDto> responseDto = notificationService.getNotifications(
            pageable, userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(responseDto));
    }

}
