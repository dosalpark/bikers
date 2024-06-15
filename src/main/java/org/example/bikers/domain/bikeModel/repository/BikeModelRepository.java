package org.example.bikers.domain.bikeModel.repository;

import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.BikeModelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeModelRepository extends JpaRepository<BikeModel, Long>,
    BikeModelRepositoryCustom {

    boolean existsBikeModelByNameEqualsAndYearEqualsAndBikeModelStatusEquals(String name, int year,
        BikeModelStatus status);
}
