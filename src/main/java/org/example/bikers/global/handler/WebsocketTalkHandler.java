package org.example.bikers.global.handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bikers.domain.talk.dto.TalkAutoSaveEventDto;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionSet.add(session);
        String getPayload = "님이 입장하셨습니다.";
        sendMessage(session, getPayload, true);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        sessionSet.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        String getPayload = message.getPayload();
        sendMessage(session, getPayload, false);
    }

    private void sendMessage(WebSocketSession session, String msg,
        boolean isConnectionEstablished) {
        String memberId = session.getAttributes().get("memberId").toString();
        String email = session.getAttributes().get("email").toString();
        String roomId = session.getAttributes().get("roomId").toString();
        TextMessage textMessage;
        if (isConnectionEstablished) {
            textMessage = new TextMessage(email + msg);
        } else {
            textMessage = new TextMessage(email + " : " + msg);
        }
        sessionSet.parallelStream().forEach(otherSession -> {
            try {
                String otherSessionRoomId = otherSession.getAttributes().get("roomId").toString();
                if (otherSession.isOpen()
                    && roomId.equals(otherSessionRoomId)) {
                    otherSession.sendMessage(textMessage);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        publisher.publishEvent(
            new TalkAutoSaveEventDto(Long.parseLong(roomId), Long.parseLong(memberId), msg));
    }

}
