package org.example.bikers.domain.bike.repository;

import org.example.bikers.domain.bike.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, Long> {

}
