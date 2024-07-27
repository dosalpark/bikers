package org.example.bikers.domain.bike.repository;

import java.util.List;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;

public interface BikeMileageRepositoryCustom {

    List<BikeMileagesGetResponseDto> getBikeMileages(Long bikeId);
}
