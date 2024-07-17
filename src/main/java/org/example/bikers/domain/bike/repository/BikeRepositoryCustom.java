package org.example.bikers.domain.bike.repository;

import java.util.List;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeRepositoryCustom {

    Slice<Bike> findAllPagable(Pageable pageable);

    List<MyBikesGetResponseDto> getMyBikes(Long memberId);

    MyBikeGetResponseDto getMyBike(Long memberId, Long bikeId);
}
