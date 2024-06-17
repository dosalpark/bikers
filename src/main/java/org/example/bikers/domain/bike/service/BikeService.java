package org.example.bikers.domain.bike.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.repository.BikeRepository;
import org.example.bikers.domain.bikeModel.service.BikeModelService;
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
}
