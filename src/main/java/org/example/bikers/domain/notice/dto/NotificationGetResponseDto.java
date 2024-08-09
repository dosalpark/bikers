package org.example.bikers.domain.notice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationGetResponseDto {

    private Long id;
    private String sender;
    private boolean isConfirm;
    private LocalDateTime createdAt;

}
