package org.example.bikers.domain.bike.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MyBikeUpdateMileageRequestDto {

    @Positive(message = "변경된 키로수를 입력해주세요")
    private int mileage;

}
