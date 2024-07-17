package org.example.bikers.domain.bike.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.bikers.domain.bikeModel.entity.BikeCategory;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;

@Getter
@Builder
public class MyBikesGetResponseDto {

    private Long bikeId;
    private Manufacturer manufacturer;
    private String bikeModelName;
    private int year;
    private BikeCategory bikeCategory;
    private int displacement;
    private String nickName;
    private int mileage;
    private String bikeStatus;
    private boolean visibility;

}
