package org.example.bikers.domain.bike.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class BikeMileageUpdateRequestDto {

    @Nullable
    private Integer preMileage;
    @Nullable
    private Integer nextMileage;
    @Nullable
    private String startPoint;
    @Nullable
    private String goalPoint;

}
