package org.example.bikers.domain.notice.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationGetResponseDto {

    private String sender;
    private String msg;
    private LocalDateTime createdAt;

}
