package org.example.bikers.global;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String email, roomId;
        try {
            email = getEmailFromRequest(request);
            roomId = getRoomIdFromRequest(request);
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 ExpiredJwtException e) {
            return false;
        }
        attributes.put("email", email);
        attributes.put("roomId", roomId);

        return true;
    }

    private String getEmailFromRequest(ServerHttpRequest request) {
        String accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
        jwtTokenProvider.validateToken(accessToken);
        Claims memberInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
        return memberInfo.get("email", String.class);
    }

    private String getRoomIdFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null && query.contains("RoomId=")) {
            return query.split("RoomId=")[1].substring(0,36);
        }
        return null;
    }

}
