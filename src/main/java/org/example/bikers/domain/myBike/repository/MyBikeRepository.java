package org.example.bikers.domain.myBike.repository;

import org.example.bikers.domain.myBike.entity.MyBike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBikeRepository extends JpaRepository<MyBike, Long> {

}
