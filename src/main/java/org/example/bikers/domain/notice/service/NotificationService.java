package org.example.bikers.domain.notice.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeExaminationDateBeforeMonthEventDto;
import org.example.bikers.domain.bike.service.BikeExaminationDateBeforeMonthResponseDto;
import org.example.bikers.domain.comment.dto.CommentCreateAuthorNoticeEventDto;
import org.example.bikers.domain.notice.dto.NotificationGetResponseDto;
import org.example.bikers.domain.notice.dto.NotificationsGetResponseDto;
import org.example.bikers.domain.notice.entity.Notification;
import org.example.bikers.domain.notice.repository.NotificationRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Transactional(readOnly = true)
    public Slice<NotificationsGetResponseDto> getNotifications(Pageable pageable, Long memberId) {
        Slice<NotificationsGetResponseDto> getNotifications = notificationRepository.getNotifications(
            pageable, memberId);
        if (getNotifications.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOTIFICATION_EMPTY);
        }
        return getNotifications;
    }

    @Transactional
    public NotificationGetResponseDto readNotification(Long memberId, Long notificationId) {
        Notification getMyNotification = notificationRepository.findByIdAndReceiverId(
                notificationId, memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NO_SUCH_NOTIFICATION));
        if (!getMyNotification.isConfirm()) {
            getMyNotification.read();
            notificationRepository.save(getMyNotification);
        }
        return NotificationGetResponseDto.builder()
            .sender(getMyNotification.getSender())
            .msg(getMyNotification.getMsg())
            .createdAt(getMyNotification.getCreatedAt())
            .build();
    }

    /*
     * BikeService.noticeExaminationDateBeforeMonth() 실행 후 이벤트방식으로 실행됨
     * 당일 알림을 받아야하는 사용자의 memberId와 바이크의 nickName 받아와서 Notification 객체 생성 후 저장
     * */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bikeExaminationDateBeforeMonthEvent(
        BikeExaminationDateBeforeMonthEventDto eventDto) {

        List<BikeExaminationDateBeforeMonthResponseDto> EventList = eventDto.getExaminationDateBeforeMonthList();
        List<Notification> addNotification = new ArrayList<>();

        for (BikeExaminationDateBeforeMonthResponseDto event : EventList) {
            String msg = "보유중인 " + event.getNickName() + "의 환경검사가 가능합니다 \n"
                + "환경검사기간은는 금일부터 두달 입니다.";
            Notification newNotice
                = new Notification(adminEmail, event.getMemberId(), msg);
            addNotification.add(newNotice);
        }

        notificationRepository.saveAll(addNotification);
    }

    /*
     * CommentService.createComment() 로 comment 저장 후 이벤트방식으로 실행됨
     * comment 작성자의 email과 post의 title, post 작성자의 memberId를 받아와서 Notification 객체 생성 후 저장
     * */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void CommentCreateAuthorNoticeEvent(CommentCreateAuthorNoticeEventDto eventDto) {
        String msg = eventDto.getCommentCreateEmail() + " 님이 " + eventDto.getPostTitle()
            + " 게시물에 댓글을 작성하였습니다.";
        Notification newNotice = new Notification(eventDto.getCommentCreateEmail(),
            eventDto.getAuthorId(), msg);
        notificationRepository.save(newNotice);
    }

}
