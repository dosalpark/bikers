package org.example.bikers.global;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.service.TalkRoomMemberService;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final TalkRoomMemberService talkRoomMemberService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Long memberId, roomId;
        String accessToken, email;
        try {
            accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
            Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
            memberId = memberInfo.get("userId", Long.class);
            email = memberInfo.get("email", String.class);
            roomId = getRoomIdFromRequest(request);

            talkRoomMemberService.validateMemberInTalkRoom(roomId, memberId);
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 ExpiredJwtException | NotFoundException e) {
            return false;
        }
        attributes.put("memberId",memberId);
        attributes.put("email", email);
        attributes.put("roomId", roomId);

        return true;
    }

    private Long getRoomIdFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null && query.contains("RoomId=")) {
            String roomId = query.split("RoomId=")[1];
            return Long.parseLong(roomId.split("&")[0]);
        }
        return null;
    }

}
