package org.example.bikers.domain.bike.repository;

import java.util.Optional;
import org.example.bikers.domain.bike.entity.BikeMileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeMileageRepository extends JpaRepository<BikeMileage, Long>,
    BikeMileageRepositoryCustom {

    Optional<BikeMileage> findByIdAndBikeId(Long bikeMileageId, Long bikeId);

}
