package org.example.bikers.domain.bike.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeCreateRequestDto;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeSellDateRequestDto;
import org.example.bikers.domain.bike.dto.MyBikeUpdateMileageRequestDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.service.BikeService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BikeController {

    private final BikeService bikeService;

    @PostMapping("/bikes")
    public ResponseEntity<Void> createMyBike(@AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody BikeCreateRequestDto requestDto) {
        bikeService.createMyBike(
            userDetails.getMember().getId(),
            requestDto.getBikeModelId(),
            requestDto.getNickName(),
            requestDto.getBikeSerialNumber().toUpperCase(),
            requestDto.getMileage(),
            requestDto.getPurchaseDate());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/bikes/{bikeId}")
    public ResponseEntity<CommonResponseDto<MyBikeGetResponseDto>> getMyBikeById(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId) {
        MyBikeGetResponseDto responseDto = bikeService.getMyBikeById(
            userDetails.getMember().getId(), bikeId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(responseDto));
    }

    @GetMapping("/bikes")
    public ResponseEntity<CommonResponseDto<List<MyBikesGetResponseDto>>> getMyBikes(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MyBikesGetResponseDto> responseDtoList = bikeService.getMyBikes(
            userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

    @PatchMapping("/bikes/{bikeId}/mileage")
    public ResponseEntity<Void> updateMyBikeMileage(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @Valid @RequestBody MyBikeUpdateMileageRequestDto requestDto) {
        bikeService.updateMyBikeMileage(
            userDetails.getMember().getId(),
            bikeId,
            requestDto.getMileage());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/bikes/{bikeId}/sell")
    public ResponseEntity<Void> sellMyBike(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @Valid @RequestBody MyBikeSellDateRequestDto requestDto) {
        bikeService.sellMyBike(
            userDetails.getMember().getId(),
            bikeId,
            requestDto.getSellDate());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/bikes/{bikeId}")
    public ResponseEntity<Void> deleteMyBike(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId) {
        bikeService.deleteMyBike(
            userDetails.getMember().getId(),
            bikeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
