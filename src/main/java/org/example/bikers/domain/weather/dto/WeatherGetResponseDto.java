package org.example.bikers.domain.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherGetResponseDto {

    private String region;
    private String precipitationProbability;
    private String precipitationType;
    private String skyCondition;
    private String nowTemp;

}
