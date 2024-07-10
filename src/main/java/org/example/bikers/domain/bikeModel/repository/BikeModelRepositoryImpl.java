package org.example.bikers.domain.bikeModel.repository;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.dto.BikeModelGetResponseDto;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.BikeModelStatus;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;
import org.example.bikers.domain.bikeModel.entity.QBikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class BikeModelRepositoryImpl implements BikeModelRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBikeModel bikeModel = QBikeModel.bikeModel;

    @Override
    public Slice<BikeModelGetResponseDto> getBikeModels(Pageable pageable, BikeModelStatus status,
        String name, String manufacturer, Integer year) {
        List<BikeModelGetResponseDto> getBikeModels = queryFactory.select(
                Projections.constructor(BikeModelGetResponseDto.class,
                    bikeModel.id,
                    bikeModel.manufacturer,
                    bikeModel.name,
                    bikeModel.year,
                    bikeModel.bikeCategory,
                    bikeModel.displacement)
            ).from(bikeModel)
            .where(
                bikeModel.bikeModelStatus.eq(status),
                nameEq(name),
                manufacturerEq(manufacturer),
                yearEq(year)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(getOrder(pageable))
            .fetch();

        boolean hasNext = getBikeModels.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            getBikeModels.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(getBikeModels, pageable, hasNext);
    }

    private BooleanExpression nameEq(String name) {
        return StringUtils.hasText(name) ? bikeModel.name.eq(name.toUpperCase()) : null;
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ? bikeModel.year.eq(year) : null;
    }

    private BooleanExpression manufacturerEq(String manufacturer) {
        return StringUtils.hasText(manufacturer) ?
            bikeModel.manufacturer.eq(Manufacturer.valueOf(manufacturer)) : null;
    }

    private OrderSpecifier<?> getOrder(Pageable pageable) {
        //Controller에서 PagebleDefault로 기본 값 들어가있음
        Sort.Order order = pageable.getSort().get().findFirst().orElse(null);
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

        PathBuilder<BikeModel> path = new PathBuilder<>(BikeModel.class, "bikeModel");
        DateTimePath<LocalDateTime> dateTimePath;
        StringPath stringPath;
        NumberPath<Integer> numberPath;


        switch (order.getProperty()) {
            case "createdAt":
                dateTimePath = path.getDateTime("createdAt", LocalDateTime.class);
                return new OrderSpecifier<>(direction, dateTimePath);
            case "name":
                stringPath = path.getString("name");
                return new OrderSpecifier<>(direction, stringPath);
            case "year":
                numberPath = path.getNumber("year", int.class);
                return new OrderSpecifier<>(direction, numberPath);
            default:
                throw new IllegalArgumentException("정렬기준이 정확하지 않습니다");
        }
    }

}
