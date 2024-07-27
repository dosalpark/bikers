package org.example.bikers.domain.bike.service;

import static org.example.bikers.global.exception.ErrorCode.BIKE_NOT_FOUND;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_BIKE;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    private static final String DELETE_STATUS = "DELETE";

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
        MyBikeGetResponseDto getMyBike = bikeRepository.getMyBike(memberId, bikeId, DELETE_STATUS);
        if (Objects.isNull(getMyBike)) {
            throw new NotFoundException(BIKE_NOT_FOUND);
        }
        return getMyBike;
    }

    @Transactional(readOnly = true)
    public List<MyBikesGetResponseDto> getMyBikes(Long memberId) {
        List<MyBikesGetResponseDto> getMyBikes = bikeRepository.getMyBikes(memberId, DELETE_STATUS);
        if (getMyBikes.isEmpty()) {
            throw new NotFoundException(BIKE_NOT_FOUND);
        }
        return getMyBikes;
    }

    public Slice<BikesGetResponseDto> getBikes(Pageable pageable, String name, String manufacturer,
        Integer year, String email) {
        Slice<BikesGetResponseDto> getBikes = bikeRepository.getBikes(pageable, name, manufacturer,
            year, email, DELETE_STATUS);
        if (getBikes.isEmpty()) {
            throw new NotFoundException(BIKE_NOT_FOUND);
        }
        return getBikes;
    }

    @Transactional
    public void updateMyBikeMileage(Long memberId, Long bikeId, int mileage, String startPoint,
        String goalPoint) {
        Bike getBike = findByMyBike(memberId, bikeId);
        if (getBike.getStatus().equals(BikeStatus.SELL)) {
            throw new IllegalArgumentException("판매한 바이크는 키로수를 변경할 수 없습니다");
        }
        if (getBike.getMileage() >= mileage) {
            throw new IllegalArgumentException("현재 키로수보다 낮게 변경 할 수 없습니다");
        }
        int preMileage = getBike.getMileage();
        getBike.updateMileage(mileage);
        bikeRepository.save(getBike);

        publisher.publishEvent(
            new UpdateMileageEvent(bikeId, preMileage, mileage, startPoint, goalPoint));
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

    @Transactional(readOnly = true)
    public Boolean validateByMyBike(Long memberId, Long bikeId) {
        return bikeRepository.existsByMemberIdEqualsAndIdEqualsAndStatusNot(
            memberId, bikeId, BikeStatus.DELETE);
    }

    private Bike findByMyBike(Long memberId, Long bikeId) {
        return bikeRepository.findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(memberId,
            bikeId, BikeStatus.DELETE).orElseThrow(() ->
            new NotFoundException(NO_SUCH_BIKE));
    }

}
