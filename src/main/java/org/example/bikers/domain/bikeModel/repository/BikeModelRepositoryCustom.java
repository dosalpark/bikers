package org.example.bikers.domain.bikeModel.repository;

import org.example.bikers.domain.bikeModel.dto.BikeModelGetResponseDto;
import org.example.bikers.domain.bikeModel.entity.BikeModelStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeModelRepositoryCustom {

    BikeModelGetResponseDto getBikeModel(Long bikeModelId, BikeModelStatus status);

    Slice<BikeModelGetResponseDto> getBikeModels(Pageable pageable, BikeModelStatus status,
        String modelName, String manufacturer, Integer year);

}
