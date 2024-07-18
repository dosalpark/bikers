package org.example.bikers.domain.bike.repository;

import java.util.List;
import org.example.bikers.domain.bike.dto.BikesGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BikeRepositoryCustom {

    List<MyBikesGetResponseDto> getMyBikes(Long memberId, String status);

    MyBikeGetResponseDto getMyBike(Long memberId, Long bikeId, String status);

    Slice<BikesGetResponseDto> getBikes(Pageable pageable, String name, String manufacturer,
        Integer year, String email, String status);

}
