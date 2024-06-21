package org.example.bikers.domain.bike.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BikesGetResponseDto {

    private Long bikeId;
    private Long memberId;
    private Long bikeModelId;
    private String nickName;
    private String bikeStatus;
    private LocalDateTime createdAt;

}
