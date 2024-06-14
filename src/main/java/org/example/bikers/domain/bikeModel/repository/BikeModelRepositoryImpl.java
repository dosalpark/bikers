package org.example.bikers.domain.bikeModel.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class BikeModelRepositoryImpl implements BikeModelRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Slice<BikeModel> findAllPagable(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BikeModel> cq = cb.createQuery(BikeModel.class);

        TypedQuery<BikeModel> query = entityManager.createQuery("SELECT bm FROM BikeModel bm",
            cq.getResultType());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize() + 1);

        List<BikeModel> result = query.getResultList();
        boolean hasNext = result.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
