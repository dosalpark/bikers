package org.example.bikers.domain.bike.service;

import static org.example.bikers.global.exception.ErrorCode.BIKE_NOT_FOUND;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_BIKE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikesGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bike.event.UpdateMileageEvent;
import org.example.bikers.domain.bike.repository.BikeRepository;
import org.example.bikers.domain.bikeModel.service.BikeModelService;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BikeService {

    private final BikeModelService bikeModelService;
    private final BikeRepository bikeRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createMyBike(Long memberId, Long bikeModelId, String nickName,
        String bikeSerialNumber, int mileage, LocalDate purchaseDate, boolean isPublic) {

        bikeModelService.validateByBikeModel(bikeModelId);

        Bike newBike = new Bike(memberId, bikeModelId, nickName, bikeSerialNumber, mileage,
            purchaseDate, isPublic);
        bikeRepository.save(newBike);
    }

    @Transactional(readOnly = true)
    public MyBikeGetResponseDto getMyBikeById(Long memberId, Long bikeId) {
        Bike getBike = findByMyBike(memberId, bikeId);
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

    public Slice<BikesGetResponseDto> getBikes(Pageable pageable) {
        Slice<Bike> getBikes = bikeRepository.findAllPagable(pageable);
        if (getBikes.isEmpty()) {
            throw new NotFoundException(BIKE_NOT_FOUND);
        }
        return conveterToDtoSlice(getBikes);
    }

    @Transactional
    public void updateMyBikeMileage(Long memberId, Long bikeId, int mileage) {
        Bike getBike = findByMyBike(memberId, bikeId);
        if (getBike.getMileage() >= mileage) {
            throw new IllegalArgumentException("현재 키로수보다 낮게 변경 할 수 없습니다");
        }
        int preMileage = getBike.getMileage();
        getBike.updateMileage(mileage);
        bikeRepository.save(getBike);

        publisher.publishEvent(
            new UpdateMileageEvent(bikeId, preMileage, mileage, "", ""));
    }

    @Transactional
    public void updateVisibility(Long memberId, Long bikeId, boolean visibility) {
        Bike getBike = findByMyBike(memberId, bikeId);
        getBike.updateVisibility(visibility);
        bikeRepository.save(getBike);
    }

    @Transactional
    public void sellMyBike(Long memberId, Long bikeId, LocalDate sellDate) {
        Bike getBike = findByMyBike(memberId, bikeId);
        if (sellDate.isBefore(getBike.getPurchaseDate())) {
            throw new IllegalArgumentException("판매일이 구입일 이전 일 수 없습니다.");
        }
        getBike.sell(sellDate);
        bikeRepository.save(getBike);
    }

    @Transactional
    public void deleteMyBike(Long memberId, Long bikeId) {
        Bike getBike = findByMyBike(memberId, bikeId);
        getBike.delete();
        bikeRepository.save(getBike);
    }

    private Bike findByMyBike(Long memberId, Long bikeId) {
        return bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(NO_SUCH_BIKE));
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
            .bikeStatus(String.valueOf(getBike.getStatus()))
            .visibility(getBike.isVisibility())
            .build();
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
                .visibility(getBike.isVisibility())
                .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    private Slice<BikesGetResponseDto> conveterToDtoSlice(Slice<Bike> getBikes) {
        return getBikes.map(getBike -> BikesGetResponseDto.builder()
            .bikeId(getBike.getId())
            .memberId(getBike.getMemberId())
            .bikeModelId(getBike.getBikeModelId())
            .nickName(getBike.getNickName())
            .bikeStatus(String.valueOf(getBike.getStatus()))
            .createdAt(getBike.getCreatedAt())
            .build());
    }

}
