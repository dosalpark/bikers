package org.example.bikers.domain.bike.repository;

import java.util.List;
import java.util.Optional;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, Long>, BikeRepositoryCustom {

    Optional<Bike> findBikeByMemberIdEqualsAndIdEqualsAndStatusNot(Long memberId, Long bikeId,
        BikeStatus status);

    List<Bike> findAllByMemberIdEqualsAndStatusNot(Long memberId, BikeStatus status);

}
