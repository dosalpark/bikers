package org.example.bikers.domain.bike.service;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeMileage;
import org.example.bikers.domain.bike.event.UpdateMileageEvent;
import org.example.bikers.domain.bike.repository.BikeMileageRepository;
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
