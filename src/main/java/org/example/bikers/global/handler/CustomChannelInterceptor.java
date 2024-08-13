package org.example.bikers.global.handler;

import io.jsonwebtoken.Claims;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bikers.domain.talk.service.TalkRoomMemberService;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final TalkRoomMemberService talkRoomMemberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = Objects.requireNonNull(
                accessor.getFirstNativeHeader("Authorization")).substring(7);
            Long roomId = Long.valueOf(
                Objects.requireNonNull(accessor.getFirstNativeHeader("RoomId")));

            jwtTokenProvider.validateToken(accessToken);
            Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
            Long memberId = memberInfo.get("userId", Long.class);
            talkRoomMemberService.validateMemberInTalkRoom(roomId, memberId);
        }

        return message;
    }
}
