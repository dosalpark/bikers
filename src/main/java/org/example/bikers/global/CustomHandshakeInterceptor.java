package org.example.bikers.global;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.service.TalkRoomService;
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
    private final TalkRoomService talkRoomService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Long memberId;
        String accessToken, email, roomId;
        try {
            accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
            Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
            memberId = memberInfo.get("userId", Long.class);
            email = memberInfo.get("email", String.class);
            roomId = getRoomIdFromRequest(request);

            talkRoomService.validateMemberInTalkRoom(roomId, memberId);
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 ExpiredJwtException | NotFoundException e) {
            return false;
        }
        attributes.put("email", email);
        attributes.put("roomId", roomId);

        return true;
    }

//    private String getEmailFromRequest(ServerHttpRequest request) {
//        String accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
//        jwtTokenProvider.validateToken(accessToken);
//        Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
//        return memberInfo.get("email", String.class);
//    }

    private String getRoomIdFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null && query.contains("RoomId=")) {
            return query.split("RoomId=")[1].substring(0, 36);
        }
        return null;
    }

}
