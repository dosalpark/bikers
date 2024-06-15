package org.example.bikers.domain.bikeModel.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class BikeModelUpdateRequestDto {

    @NotEmpty
    private String manufacturer;

    @NotEmpty
    private String name;

    @Pattern(regexp = "^[1-9][0-9][0-9][0-9]$", message = " ex)1992 양식으로 입력 해 주세요")
    private String year;

    @NotEmpty
    private String bikeCategory;

    @Positive
    private int displacement;

}
