package org.example.bikers.domain.bikeModel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BikeModelGetResponseDto {

    private Long bikeModelId;
    private String manufacturer;
    private String name;
    private int year;
    private String bikeCategory;
    private int displacement;

}
