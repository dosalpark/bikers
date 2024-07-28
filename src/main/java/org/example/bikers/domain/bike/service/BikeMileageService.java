package org.example.bikers.domain.bike.service;

import static org.example.bikers.global.exception.ErrorCode.NO_BIKE_MILEAGE_FOUND;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_BIKE_MILEAGE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;
import org.example.bikers.domain.bike.entity.BikeMileage;
import org.example.bikers.domain.bike.event.UpdateMileageEvent;
import org.example.bikers.domain.bike.repository.BikeMileageRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class BikeMileageService {

    private final BikeMileageRepository bikeMileageRepository;
    private final BikeService bikeService;

    @Transactional(readOnly = true)
    public List<BikeMileagesGetResponseDto> getBikeMileages(Long memberId, Long bikeId) {
        if (!bikeService.validateByMyBike(memberId, bikeId)) {
            throw new NotFoundException(ErrorCode.NO_SUCH_BIKE);
        }
        List<BikeMileagesGetResponseDto> getBikeMileages = bikeMileageRepository.getBikeMileages(
            bikeId);
        if (getBikeMileages.isEmpty()) {
            throw new NotFoundException(NO_BIKE_MILEAGE_FOUND);
        }
        return getBikeMileages;
    }

    @Transactional
    public void createBikeMileage(Long memberId, Long bikeId, int preMileage, int nextMileage,
        String startPoint, String goalPoint) {
        if (preMileage > nextMileage) {
            throw new IllegalArgumentException("운행 전 키로수가 더 높습니다.");
        }
        if (!bikeService.validateByMyBike(memberId, bikeId)) {
            throw new NotFoundException(ErrorCode.NO_SUCH_BIKE);
        }
        BikeMileage newBikeMileage = new BikeMileage(
            bikeId,
            preMileage,
            nextMileage,
            startPoint,
            goalPoint);
        bikeMileageRepository.save(newBikeMileage);
    }

    @Transactional
    public void updateBikeMileage(Long memberId, Long bikeId, Long bikeMileageId,
        Integer preMileage, Integer nextMileage, String startPoint, String goalPoint) {

        if (!bikeService.validateByMyBike(memberId, bikeId)) {
            throw new NotFoundException(ErrorCode.NO_SUCH_BIKE);
        }
        BikeMileage getBikeMileage = bikeMileageRepository.findById(bikeMileageId).orElseThrow(
            () -> new NotFoundException(NO_SUCH_BIKE_MILEAGE));

        if (preMileage != null && nextMileage != null) {
            if (preMileage > nextMileage) {
                throw new IllegalArgumentException("운행 전 키로수가 더 높습니다.");
            }
        } else {
            if (nextMileage != null) {
                if (getBikeMileage.getPreMileage() >= nextMileage) {
                    throw new IllegalArgumentException("운행 전 키로수보다 현재 키로수가 더 낮습니다.");
                }
            }
            if (preMileage != null) {
                if (getBikeMileage.getNowMileage() < preMileage) {
                    throw new IllegalArgumentException("운행 후 키로수보다 운행 전 키로수가 더 큽니다.");
                }
            }
        }

        getBikeMileage.update(preMileage, nextMileage, startPoint, goalPoint);
        bikeMileageRepository.save(getBikeMileage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void addMileageHistory(UpdateMileageEvent mileageEvent) {
        BikeMileage newBikeMileage = new BikeMileage(
            mileageEvent.getBikeId(),
            mileageEvent.getPreMileage(),
            mileageEvent.getNowMileage(),
            mileageEvent.getStartPoint(),
            mileageEvent.getGoalPoint());
        bikeMileageRepository.save(newBikeMileage);
    }


}
