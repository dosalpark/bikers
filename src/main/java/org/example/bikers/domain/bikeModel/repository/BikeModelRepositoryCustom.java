package org.example.bikers.domain.bikeModel.repository;

import com.querydsl.core.types.Predicate;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeModelRepositoryCustom {

    Slice<BikeModel> getBikeModels(Pageable pageable, Predicate predicate);

}
