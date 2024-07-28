package org.example.bikers.domain.bike.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeMileageCreateRequestDto;
import org.example.bikers.domain.bike.dto.BikeMileageUpdateRequestDto;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;
import org.example.bikers.domain.bike.service.BikeMileageService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Void> createBikeMileage(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @Valid @RequestBody BikeMileageCreateRequestDto requestDto) {
        bikeMileageService.createBikeMileage(
            userDetails.getMember().getId(),
            bikeId,
            requestDto.getPreMileage(),
            requestDto.getNextMileage(),
            requestDto.getStartPoint(),
            requestDto.getGoalPoint());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{bikeMileageId}")
    public ResponseEntity<Void> updateBikeMileage(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @PathVariable Long bikeMileageId,
        @Valid @RequestBody BikeMileageUpdateRequestDto requestDto) {
        bikeMileageService.updateBikeMileage(
            userDetails.getMember().getId(),
            bikeId,
            bikeMileageId,
            requestDto.getPreMileage(),
            requestDto.getNextMileage(),
            requestDto.getStartPoint(),
            requestDto.getGoalPoint());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
