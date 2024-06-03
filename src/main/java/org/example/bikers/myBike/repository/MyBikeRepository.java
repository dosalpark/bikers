package org.example.bikers.myBike.repository;

import org.example.bikers.myBike.entity.MyBike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBikeRepository extends JpaRepository<MyBike, Long> {

}
