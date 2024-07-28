package org.example.bikers.domain.bike.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class BikeMileageCreateRequestDto {

    @Positive(message = "운행 전 키로수를 입력해주세요")
    private int preMileage;
    @Positive(message = "운행 후 키로수를 입력해주세요")
    private int nextMileage;
    @Nullable
    private String startPoint;
    @Nullable
    private String goalPoint;

}
