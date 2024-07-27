package org.example.bikers.domain.bike.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeMileagesGetResponseDto;
import org.example.bikers.domain.bike.entity.QBikeMileage;

@RequiredArgsConstructor
public class BikeMileageRepositoryImpl implements BikeMileageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBikeMileage bikeMileage = QBikeMileage.bikeMileage;


    @Override
    public List<BikeMileagesGetResponseDto> getBikeMileages(Long bikeId) {
        return queryFactory.select(
                Projections.constructor(BikeMileagesGetResponseDto.class,
                    bikeMileage.bikeId,
                    bikeMileage.preMileage,
                    bikeMileage.nowMileage,
                    bikeMileage.startPoint,
                    bikeMileage.goalPoint,
                    bikeMileage.createdAt,
                    bikeMileage.modifiedAt)
            ).from(bikeMileage)
            .where(
                bikeMileage.bikeId.eq(bikeId)
            )
            .orderBy(bikeMileage.createdAt.desc())
            .fetch();
    }

}
