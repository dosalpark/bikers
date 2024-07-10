package org.example.bikers.domain.bikeModel.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.QBikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class BikeModelRepositoryImpl implements BikeModelRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBikeModel bikeModel = QBikeModel.bikeModel;

    public Slice<BikeModel> findAll(Pageable pageable) {
        List<BikeModel> getBikeModels = queryFactory.select(bikeModel).from(bikeModel)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();
        boolean hasNext = getBikeModels.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            getBikeModels.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(getBikeModels, pageable, hasNext);
    }

}
