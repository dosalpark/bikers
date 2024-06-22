package org.example.bikers.domain.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Slice<Post> findAllPagable(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);

        Order order = pageable.getSort().stream().findFirst().orElse(null);
        String orderBuild = " ORDER BY " + order.getProperty() + " " + order.getDirection();

        TypedQuery<Post> query = entityManager.createQuery(
            "SELECT p FROM Post p WHERE p.status != 'DELETE' " + orderBuild,
            cq.getResultType());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize() + 1);

        List<Post> result = query.getResultList();
        boolean hasNext = result.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
