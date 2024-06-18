package org.example.bikers.domain.bike.service;

import static org.example.bikers.global.exception.ErrorCode.BIKE_NOT_FOUND;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_BIKE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bike.repository.BikeRepository;
import org.example.bikers.domain.bikeModel.service.BikeModelService;
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
            new NotFoundException(NO_SUCH_BIKE)
        );
        return converterToDto(getBike);
    }

    @Transactional(readOnly = true)
    public List<MyBikesGetResponseDto> getMyBikes(Long memberId) {
        List<Bike> getBikes = bikeRepository.findAllByMemberIdEqualsAndStatusNot(memberId,
            BikeStatus.DELETE);
        if (getBikes.isEmpty()) {
            throw new NotFoundException(BIKE_NOT_FOUND);
        }
        return converterToDtoList(getBikes);
    }

    @Transactional
    public void updateMyBikeMileage(Long memberId, Long bikeId, int mileage) {
        Bike getBike = bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(NO_SUCH_BIKE)
        );
        if (getBike.getMileage() >= mileage) {
            throw new IllegalArgumentException("현재 키로수보다 낮게 변경 할 수 없습니다");
        }
        getBike.updateMileage(mileage);
        bikeRepository.save(getBike);
    }

    @Transactional
    public void sellMyBike(Long memberId, Long bikeId, LocalDate sellDate) {
        Bike getBike = bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(NO_SUCH_BIKE)
        );
        getBike.sell(sellDate);
        bikeRepository.save(getBike);
    }

    @Transactional
    public void deleteMyBike(Long memberId, Long bikeId) {
        Bike getBike = bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(NO_SUCH_BIKE)
        );
        getBike.delete();
        bikeRepository.save(getBike);
    }

    private MyBikeGetResponseDto converterToDto(Bike getBike) {
        return MyBikeGetResponseDto.builder()
            .bikeId(getBike.getId())
            .bikeModelId(getBike.getBikeModelId())
            .nickName(getBike.getNickName())
            .bikeSerialNumber(getBike.getBikeSerialNumber())
            .mileage(getBike.getMileage())
            .purchaseDate(getBike.getPurchaseDate())
            .sellDate(getBike.getSellDate())
            .bikeStatus(String.valueOf(getBike.getStatus())).build();
    }

    private List<MyBikesGetResponseDto> converterToDtoList(List<Bike> getBikes) {
        List<MyBikesGetResponseDto> responseDtoList = new ArrayList<>();
        for (Bike getBike : getBikes) {
            MyBikesGetResponseDto responseDto = MyBikesGetResponseDto.builder()
                .bikeId(getBike.getId())
                .bikeModelId(getBike.getBikeModelId())
                .nickName(getBike.getNickName())
                .mileage(getBike.getMileage())
                .bikeStatus(String.valueOf(getBike.getStatus()))
                .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

}
