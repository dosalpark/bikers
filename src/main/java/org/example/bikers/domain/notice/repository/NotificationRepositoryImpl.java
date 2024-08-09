package org.example.bikers.domain.notice.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.notice.dto.NotificationGetResponseDto;
import org.example.bikers.domain.notice.entity.QNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QNotification notification = QNotification.notification;

    @Override
    public Slice<NotificationGetResponseDto> getNotifications(Pageable pageable, Long memberId) {
        List<NotificationGetResponseDto> getNotifications = queryFactory.select(
                Projections.constructor(NotificationGetResponseDto.class,
                    notification.id,
                    notification.sender,
                    notification.isConfirm,
                    notification.createdAt)
            ).from(notification)
            .where(notification.receiverId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(
                notification.isConfirm.asc(),
                notification.createdAt.desc())
            .fetch();
        boolean hasNext = getNotifications.size() == pageable.getPageSize() + 1;

        if (hasNext) {
            getNotifications.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(getNotifications, pageable, hasNext);
    }
}
