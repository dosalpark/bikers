package org.example.bikers.domain.bike.repository;

import org.example.bikers.domain.bike.entity.Bike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeRepositoryCustom {

    Slice<Bike> findAllPagable(Pageable pageable);

}
