package org.example.bikers.domain.bike.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.example.bikers.domain.bikeModel.entity.Manufacturer;
import org.example.bikers.domain.bikeModel.entity.QBikeModel;
import org.example.bikers.domain.member.entity.QMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBike bike = QBike.bike;
    private final QBikeModel bikeModel = QBikeModel.bikeModel;
    private final QMember member = QMember.member;


    @Override
    public List<MyBikesGetResponseDto> getMyBikes(Long memberId, String status) {
        return queryFactory.select(
                Projections.constructor(MyBikesGetResponseDto.class,
                    bike.id,
                    bikeModel.manufacturer,
                    bikeModel.name,
                    bikeModel.year,
                    bikeModel.bikeCategory,
                    bikeModel.displacement,
                    bike.nickName,
                    bike.status,
                    bike.visibility)
            ).from(bike)
            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
            .where(
                bike.memberId.eq(memberId),
                statusNe(status)
            )
            .fetch();
    }

    @Override
    public MyBikeGetResponseDto getMyBike(Long memberId, Long bikeId, String status) {
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
                    bike.purchaseDate,
                    bike.sellDate,
                    bike.status,
                    bike.visibility)
            ).from(bike)
            .leftJoin(bikeModel).on(bike.bikeModelId.eq(bikeModel.id))
            .where(
                bike.memberId.eq(memberId),
                bike.id.eq(bikeId),
                statusNe(status)
            )
            .fetchOne();
    }

    @Override
    public Slice<BikesGetResponseDto> getBikes(Pageable pageable, String name, String manufacturer,
        Integer year, String email, String status) {
        List<BikesGetResponseDto> getBikes = queryFactory.select(
                Projections.constructor(BikesGetResponseDto.class,
                    bike.id,
                    member.email,
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
            .leftJoin(member).on(bike.memberId.eq(member.id))
            .where(
                bike.visibility.eq(true),
                statusNe(status),
                bikeModelNameEq(name),
                manufacturerEq(manufacturer),
                bikeModelYearEq(year),
                ownerEmailEq(email)
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

    private BooleanExpression bikeModelNameEq(String name) {
        return StringUtils.hasText(name) ? bikeModel.name.eq(name) : null;
    }

    private BooleanExpression manufacturerEq(String manufacturer) {
        return StringUtils.hasText(manufacturer) ?
            bikeModel.manufacturer.eq(Manufacturer.valueOf(manufacturer.toUpperCase())) : null;
    }

    private BooleanExpression bikeModelYearEq(Integer year) {
        return year != null ? bikeModel.year.eq(year) : null;
    }

    private BooleanExpression ownerEmailEq(String email) {
        return StringUtils.hasText(email) ? member.email.eq(email) : null;
    }

    private Predicate statusNe(String bikeStatus) {
        return StringUtils.hasText(bikeStatus) ?
            bike.status.ne(BikeStatus.valueOf(bikeStatus)) : null;
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
