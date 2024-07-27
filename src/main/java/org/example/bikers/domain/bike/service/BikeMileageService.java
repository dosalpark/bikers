package org.example.bikers.domain.bike.service;

import static org.example.bikers.global.exception.ErrorCode.NO_BIKE_MILEAGE_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeMileage;
import org.example.bikers.domain.bike.event.UpdateMileageEvent;
import org.example.bikers.domain.bike.repository.BikeMileageRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.context.event.EventListener;
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

    @Transactional
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

    @EventListener
    public void test(Bike bike) {
        System.out.println("bike.getMileage() = " + bike.getMileage());
    }

}
