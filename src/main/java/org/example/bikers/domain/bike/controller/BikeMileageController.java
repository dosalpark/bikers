package org.example.bikers.domain.bike.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;
import org.example.bikers.domain.bike.service.BikeMileageService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bike/{bikeId}/mileage")
public class BikeMileageController {

    private final BikeMileageService bikeMileageService;

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<BikeMileagesGetResponseDto>>> getBikeMileages(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId) {
        List<BikeMileagesGetResponseDto> responseDtoList = bikeMileageService.getBikeMileages(
            userDetails.getMember().getId(),
            bikeId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

}
