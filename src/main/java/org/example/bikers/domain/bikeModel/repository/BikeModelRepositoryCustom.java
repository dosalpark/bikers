package org.example.bikers.domain.bikeModel.repository;

import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.BikeModelStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeModelRepositoryCustom {

    Slice<BikeModel> getBikeModels(Pageable pageable, BikeModelStatus status,
        String modelName, String manufacturer, Integer year);

}
