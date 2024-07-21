package org.example.bikers.domain.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.comment.entity.QComment;
import org.example.bikers.domain.member.entity.QMember;
import org.example.bikers.domain.post.dto.PostGetResponseDto;
import org.example.bikers.domain.post.dto.PostsGetResponseDto;
import org.example.bikers.domain.post.entity.Post;
import org.example.bikers.domain.post.entity.PostStatus;
import org.example.bikers.domain.post.entity.QPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPost post = QPost.post;
    private final QComment comment = QComment.comment;
    private final QMember member = QMember.member;

    @Override
    public Slice<PostsGetResponseDto> getPosts(Pageable pageable, String email, String title,
        String status) {
        List<PostsGetResponseDto> getPosts = queryFactory.select(
                Projections.constructor(PostsGetResponseDto.class,
                    post.id,
                    post.title,
                    post.memberId,
                    getCommentCountByPostId(post.id),
                    post.createdAt)
            )
            .from(post)
            .where(
                emailEq(email),
                titleContainsIgnoreCase(title),
                statusNe(status)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(getOrder(pageable))
            .fetch();

        boolean hasNext = getPosts.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            getPosts.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(getPosts, pageable, hasNext);
    }

    @Override
    public PostGetResponseDto getPost(Long postId, String status) {
        return queryFactory.select(
                Projections.constructor(PostGetResponseDto.class,
                    post.memberId,
                    post.title,
                    post.content,
                    post.createdAt,
                    post.modifiedAt)
            )
            .from(post)
            .where(
                post.id.eq(postId),
                statusNe(status)
            )
            .fetchOne();
    }

    private BooleanExpression statusNe(String status) {
        return StringUtils.hasText(status) ?
            post.status.ne(PostStatus.valueOf(status)) : null;
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ?
            post.memberId.eq(
                queryFactory.select(member.id).from(member).where(member.email.eq(email)))
            : null;
    }

    private BooleanExpression titleContainsIgnoreCase(String title) {
        return StringUtils.hasText(title) ? post.title.containsIgnoreCase(title) : null;
    }

    private JPAQuery<Long> getCommentCountByPostId(NumberPath<Long> id) {
        return queryFactory.select(comment.count()).from(comment).where(comment.postId.eq(id));
    }

    private OrderSpecifier<?> getOrder(Pageable pageable) {
        Sort.Order order = pageable.getSort().get().findFirst().orElse(null);
        com.querydsl.core.types.Order direction =
            order.getDirection().isAscending() ? com.querydsl.core.types.Order.ASC
                : com.querydsl.core.types.Order.DESC;

        PathBuilder<Post> path = new PathBuilder<>(Post.class, "post");
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
