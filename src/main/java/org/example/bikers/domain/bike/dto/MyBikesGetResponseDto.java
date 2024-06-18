package org.example.bikers.domain.bike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBikesGetResponseDto {

    private Long bikeId;
    private Long bikeModelId;
    private String nickName;
    private int mileage;
    private String bikeStatus;


}
