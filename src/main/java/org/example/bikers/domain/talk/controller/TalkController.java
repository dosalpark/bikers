package org.example.bikers.domain.talk.controller;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.TalkDto;
import org.example.bikers.domain.talk.service.TalkService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TalkController {

    private final TalkService talkService;

    @MessageMapping("/talk-rooms/{roomId}")
    public void sendTalk(TalkDto talkDto, @DestinationVariable Long roomId) {
        talkService.sendTalk(
            talkDto.getAccessToken(),
            talkDto.getMsg(),
            roomId);
    }

}
