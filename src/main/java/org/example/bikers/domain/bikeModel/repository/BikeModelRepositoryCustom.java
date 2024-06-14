package org.example.bikers.domain.bikeModel.repository;

import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeModelRepositoryCustom {

    Slice<BikeModel> findAllPagable(Pageable pageable);

}
