package org.example.bikers.domain.bikeModel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bikers.domain.bikeModel.entity.BikeCategory;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BikeModelGetResponseDto {

    private Long bikeModelId;
    private Manufacturer manufacturer;
    private String name;
    private int year;
    private BikeCategory bikeCategory;
    private int displacement;

}
