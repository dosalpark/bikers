package org.example.bikers.domain.talk.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.JoinTalkRoomHistoryEventDto;
import org.example.bikers.domain.talk.dto.JoinTalkRoomSendEventDto;
import org.example.bikers.domain.talk.dto.LeaveTalkRoomHistoryEventDto;
import org.example.bikers.domain.talk.dto.LeaveTalkRoomSendEventDto;
import org.example.bikers.domain.talk.dto.TalkAutoSaveEventDto;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class TalkService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ApplicationEventPublisher publisher;
    private final JwtTokenProvider jwtTokenProvider;

    public void sendTalk(String accessToken, String msg, Long roomId) {
        Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken.substring(7));
        Long sendMemberId = memberInfo.get("userId", Long.class);
        String sendMemberEmail = memberInfo.get("email", String.class);

        messagingTemplate.convertAndSend("/sub/talk-rooms/" + roomId,
            sendMemberEmail + " : " + msg);

        publisher.publishEvent(
            new TalkAutoSaveEventDto(roomId, sendMemberId, msg));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void JoinNoticeSendTalk(JoinTalkRoomSendEventDto joinTalkRoomSendEventDto) {
        String msg = joinTalkRoomSendEventDto.getMemberEmail() + " 님이 채팅방에 들어왔습니다.";
        messagingTemplate.convertAndSend("/sub/talk-rooms/" + joinTalkRoomSendEventDto.getRoomId(),
            msg);

        publisher.publishEvent(
            new JoinTalkRoomHistoryEventDto(joinTalkRoomSendEventDto.getRoomId(),
                joinTalkRoomSendEventDto.getMemberId(), msg));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void leaveNoticeSendTalk(LeaveTalkRoomSendEventDto leaveTalkRoomSendEventDto) {
        String msg = leaveTalkRoomSendEventDto.getMemberEmail() + " 님이 채팅방에서 나갔습니다.";
        messagingTemplate.convertAndSend("/sub/talk-rooms/" + leaveTalkRoomSendEventDto.getRoomId(),
            msg);

        publisher.publishEvent(
            new LeaveTalkRoomHistoryEventDto(leaveTalkRoomSendEventDto.getRoomId(),
                leaveTalkRoomSendEventDto.getMemberId(), msg));
    }

}
