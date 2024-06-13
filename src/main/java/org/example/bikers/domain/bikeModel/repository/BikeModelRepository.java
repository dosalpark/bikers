package org.example.bikers.domain.bikeModel.repository;

import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeModelRepository extends JpaRepository<BikeModel, Long> {

    boolean existsBikeModelByNameEqualsAndYearEquals(String name, int year);
}
