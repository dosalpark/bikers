package org.example.bikers.domain.bike.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bikeModel.entity.BikeCategory;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBikesGetResponseDto {

    private Long bikeId;
    private Manufacturer manufacturer;
    private String bikeModelName;
    private int year;
    private BikeCategory bikeCategory;
    private int displacement;
    private String nickName;
    private BikeStatus bikeStatus;
    private boolean visibility;
    private LocalDate examinationDate;

}
