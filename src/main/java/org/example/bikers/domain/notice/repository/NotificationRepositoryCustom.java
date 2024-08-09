package org.example.bikers.domain.notice.repository;

import org.example.bikers.domain.notice.dto.NotificationsGetResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationRepositoryCustom {

    Slice<NotificationsGetResponseDto> getNotifications(Pageable pageable, Long memberId);

}
