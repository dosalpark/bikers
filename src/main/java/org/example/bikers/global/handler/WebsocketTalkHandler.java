package org.example.bikers.global.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketTalkHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessionSet = new HashSet<>();
    private final Map<Long, Set<WebSocketSession>> talkRoomMap = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //커넥션 연결
        log.info("{} 접속", session.getId());
        sessionSet.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        //커넥션 종료
        log.info("{} 종료", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        //메세지 전송부

        //1. 이용자가 보낸 메세지
        String getPayload = message.getPayload();
        //2. 이용자 메세지를 보여줌
        log.info("{}님: {}", session.getId(), getPayload);
        //3. 서버가 이용자에게 보낼 메세지 생성
        TextMessage msg = new TextMessage("server: hello");
        //4. 이용자에게 발송
        session.sendMessage(msg);
    }

}
