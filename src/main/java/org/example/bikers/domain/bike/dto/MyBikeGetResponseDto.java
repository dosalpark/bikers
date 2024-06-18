package org.example.bikers.domain.bike.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBikeGetResponseDto {

    private Long bikeId;
    private Long bikeModelId;
    private String nickName;
    private String bikeSerialNumber;
    private int mileage;
    private LocalDate purchaseDate;
    private LocalDate saleDate;
    private String bikeStatus;

}
