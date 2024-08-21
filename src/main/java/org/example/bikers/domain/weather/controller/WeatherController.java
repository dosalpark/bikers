package org.example.bikers.domain.weather.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.weather.dto.WeatherGetResponseDto;
import org.example.bikers.domain.weather.service.WeatherService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weathers")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<WeatherGetResponseDto>>> getWeathers() {
        List<WeatherGetResponseDto> responseDtoList = weatherService.getWeathers();

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

}
