package org.example.bikers.domain.bike.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikesGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bike.entity.QBike;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.QBikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBike bike = QBike.bike;
    private final QBikeModel bikeModel = QBikeModel.bikeModel;


    @Override
    public List<MyBikesGetResponseDto> getMyBikes(Long memberId) {
        return queryFactory.select(
                Projections.constructor(MyBikesGetResponseDto.class,
                    bike.id,
                    bikeModel.manufacturer,
                    bikeModel.name,
                    bikeModel.year,
                    bikeModel.bikeCategory,
                    bikeModel.displacement,
                    bike.nickName,
                    bike.mileage,
                    bike.status,
                    bike.visibility)
            ).from(bike)
            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
            .where(
                bike.memberId.eq(memberId),
                bike.status.ne(BikeStatus.DELETE)
            )
            .fetch();
    }

    @Override
    public MyBikeGetResponseDto getMyBike(Long memberId, Long bikeId) {
        return queryFactory.select(
                Projections.constructor(MyBikeGetResponseDto.class,
                    bike.id,
                    bikeModel.manufacturer,
                    bikeModel.name,
                    bikeModel.year,
                    bikeModel.bikeCategory,
                    bikeModel.displacement,
                    bike.nickName,
                    bike.bikeSerialNumber,
                    bike.mileage,
                    bike.purchaseDate,
                    bike.sellDate,
                    bike.status,
                    bike.visibility)
            ).from(bike)
            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
            .where(
                bike.memberId.eq(memberId),
                bike.id.eq(bikeId),
                bike.status.ne(BikeStatus.DELETE)
            )
            .fetchOne();
    }


    @Override
    public Slice<BikesGetResponseDto> getBikes(Pageable pageable) {
        List<BikesGetResponseDto> getBikes = queryFactory.select(
                Projections.constructor(BikesGetResponseDto.class,
                    bike.id,
                    bike.memberId,
                    bikeModel.manufacturer,
                    bikeModel.name,
                    bikeModel.year,
                    bikeModel.bikeCategory,
                    bikeModel.displacement,
                    bike.nickName,
                    bike.status,
                    bike.createdAt)
            ).from(bike)
            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
            .where(
                bike.status.ne(BikeStatus.DELETE),
                bike.visibility.eq(true)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(getOrder(pageable))
            .fetch();

        boolean hasNext = getBikes.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            getBikes.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(getBikes, pageable, hasNext);
    }

    private OrderSpecifier<?> getOrder(Pageable pageable) {
        Sort.Order order = pageable.getSort().get().findFirst().orElse(null);
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

        PathBuilder<BikeModel> path = new PathBuilder<>(BikeModel.class, "bikeModel");
        DateTimePath<LocalDateTime> dateTimePath;

        switch (order.getProperty()) {
            case "createdAt":
                dateTimePath = path.getDateTime("createdAt", LocalDateTime.class);
                return new OrderSpecifier<>(direction, dateTimePath);
            case "modifiedAt":
                dateTimePath = path.getDateTime("modifiedAt", LocalDateTime.class);
                return new OrderSpecifier<>(direction, dateTimePath);
            default:
                throw new IllegalArgumentException("정렬기준이 정확하지 않습니다");
        }
    }

}
