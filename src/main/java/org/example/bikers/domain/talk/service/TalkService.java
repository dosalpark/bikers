package org.example.bikers.domain.talk.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.TalkAutoSaveEventDto;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

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

}
