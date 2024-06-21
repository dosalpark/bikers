package org.example.bikers.domain.bike.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.entity.Bike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepositoryCustom {

    private final EntityManager entityManager;

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
