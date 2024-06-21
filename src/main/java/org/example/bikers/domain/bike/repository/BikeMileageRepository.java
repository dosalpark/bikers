package org.example.bikers.domain.bike.repository;

import org.example.bikers.domain.bike.entity.BikeMileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeMileageRepository extends JpaRepository<BikeMileage, Long> {

}
