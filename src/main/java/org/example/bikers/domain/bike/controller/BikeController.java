package org.example.bikers.domain.bike.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeCreateRequestDto;
import org.example.bikers.domain.bike.dto.BikesGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeUpdateExaminationDateRequestDto;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeSellDateRequestDto;
import org.example.bikers.domain.bike.dto.MyBikeUpdateVisibilityRequestDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.service.BikeService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
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
            requestDto.getPurchaseDate(),
            requestDto.isVisibility());

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

    @GetMapping("/bikes/visible")
    public ResponseEntity<CommonResponseDto<Slice<BikesGetResponseDto>>> getBikes(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String manufacturer,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) String email) {
        Slice<BikesGetResponseDto> responseDtoList = bikeService.getBikes(
            pageable,
            name,
            manufacturer,
            year,
            email);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

    @PatchMapping("/bikes/{bikeId}/visibility")
    public ResponseEntity<Void> updateVisibility(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @Valid @RequestBody MyBikeUpdateVisibilityRequestDto requestDto) {
        bikeService.updateVisibility(
            userDetails.getMember().getId(),
            bikeId,
            requestDto.isVisibility());

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

    @PatchMapping("/bikes/{bikeId}/examination-date")
    public ResponseEntity<Void> updateExaminationDate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long bikeId,
        @RequestBody MyBikeUpdateExaminationDateRequestDto requestDto) {
        bikeService.updateExaminationDate(
            userDetails.getMember().getId(),
            bikeId,
            requestDto.getExaminationDate());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
