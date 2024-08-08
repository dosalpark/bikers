package org.example.bikers.domain.notice.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.dto.BikeExaminationDateBeforeMonthEventDto;
import org.example.bikers.domain.bike.service.BikeExaminationDateBeforeMonthResponseDto;
import org.example.bikers.domain.notice.entity.Notification;
import org.example.bikers.domain.notice.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bikeExaminationDateBeforeMonthEvent(
        BikeExaminationDateBeforeMonthEventDto eventDto) {

        List<BikeExaminationDateBeforeMonthResponseDto> EventList = eventDto.getExaminationDateBeforeMonthList();
        List<Notification> addNotification = new ArrayList<>();

        for (BikeExaminationDateBeforeMonthResponseDto event : EventList) {
            String msg = "보유중인 " + event.getNickName() + "의 환경검사가 가능합니다 \n"
                + "환경검사기간은는 금일부터 두달 입니다.";
            Notification newNotice = new Notification(event.getMemberId(), msg);
            addNotification.add(newNotice);
        }

        notificationRepository.saveAll(addNotification);
    }

}
