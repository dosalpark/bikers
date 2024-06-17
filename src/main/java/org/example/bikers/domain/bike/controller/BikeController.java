package org.example.bikers.domain.bike.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeCreateRequestDto;
import org.example.bikers.domain.bike.service.BikeService;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
}
