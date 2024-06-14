package org.example.bikers.domain.bikeModel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.dto.BikeModelCreateRequestDto;
import org.example.bikers.domain.bikeModel.dto.BikeModelGetResponseDto;
import org.example.bikers.domain.bikeModel.service.BikeModelService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bike-models")
public class BikeModelController {

    private final BikeModelService bikeModelService;

    //모델 등록
    @PostMapping
    public ResponseEntity<Void> createBikeModel(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody BikeModelCreateRequestDto requestDto) {
        bikeModelService.createBikeModel(
            userDetails.getMember().getId(),
            requestDto.getManufacturer().toUpperCase(),
            requestDto.getName().toUpperCase(),
            Integer.parseInt(requestDto.getYear()),
            requestDto.getBikeCategory().toUpperCase(),
            requestDto.getDisplacement()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //모델 조회
    @GetMapping("/{bikeModelId}")
    public ResponseEntity<CommonResponseDto<BikeModelGetResponseDto>> getBikeModelById(
        @PathVariable Long bikeModelId) {
        BikeModelGetResponseDto responseDto = bikeModelService.getBikeModelById(bikeModelId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(responseDto));
    }

    //모델 전체조회
    @GetMapping
    public ResponseEntity<CommonResponseDto<Slice<BikeModelGetResponseDto>>> getBikeModels(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Slice<BikeModelGetResponseDto> responseDtoList = bikeModelService.getBikeModels(pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }


    //모델 수정

    //모델 삭제

}
