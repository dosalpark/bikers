package org.example.bikers.domain.bike.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bike.repository.BikeRepository;
import org.example.bikers.domain.bikeModel.service.BikeModelService;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BikeService {

    private final BikeModelService bikeModelService;
    private final BikeRepository bikeRepository;

    @Transactional(readOnly = true)
    public void createMyBike(Long memberId, Long bikeModelId, String nickName,
        String bikeSerialNumber, int mileage, LocalDate purchaseDate) {

        bikeModelService.validateByBikeModel(bikeModelId);

        Bike newBike = new Bike(memberId, bikeModelId, nickName, bikeSerialNumber, mileage,
            purchaseDate);
        bikeRepository.save(newBike);
    }

    @Transactional(readOnly = true)
    public MyBikeGetResponseDto getMyBikeById(Long memberId, Long bikeId) {
        Bike getBike = bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(ErrorCode.NO_SUCH_BIKE)
        );
        return converterToDto(getBike);
    }

    private MyBikeGetResponseDto converterToDto(Bike getBike) {
        return MyBikeGetResponseDto.builder()
            .bikeId(getBike.getId())
            .bikeModelId(getBike.getBikeModelId())
            .nickName(getBike.getNickName())
            .bikeSerialNumber(getBike.getBikeSerialNumber())
            .mileage(getBike.getMileage())
            .purchaseDate(getBike.getPurchaseDate())
            .saleDate(getBike.getSaleDate())
            .bikeStatus(String.valueOf(getBike.getStatus())).build();
    }
}
