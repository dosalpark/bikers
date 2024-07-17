package org.example.bikers.domain.bike.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.MyBikeGetResponseDto;
import org.example.bikers.domain.bike.dto.MyBikesGetResponseDto;
import org.example.bikers.domain.bike.entity.Bike;
import org.example.bikers.domain.bike.entity.BikeStatus;
import org.example.bikers.domain.bike.entity.QBike;
import org.example.bikers.domain.bikeModel.entity.QBikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepositoryCustom {

    private final EntityManager entityManager;
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
    public Slice<Bike> findAllPagable(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bike> cq = cb.createQuery(Bike.class);

        Order order = pageable.getSort().stream().findFirst().orElse(null);
        String orderBuild = " ORDER BY " + order.getProperty() + " " + order.getDirection();

        TypedQuery<Bike> query = entityManager.createQuery(
            "SELECT b FROM Bike b WHERE b.status != 'DELETE' AND b.visibility = true " + orderBuild,
            cq.getResultType());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize() + 1);

        List<Bike> result = query.getResultList();
        boolean hasNext = result.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
