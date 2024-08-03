package org.example.bikers.domain.talk.service;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.TalkAutoSaveEventDto;
import org.example.bikers.domain.talk.entity.TalkHistory;
import org.example.bikers.domain.talk.repository.TalkHistoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalkHistoryService {

    private final TalkHistoryRepository talkHistoryRepository;

    @EventListener
    @Transactional
    public void wsTalkAutoSave(TalkAutoSaveEventDto talkAutoSaveEventDto) {
        Long roomId = talkAutoSaveEventDto.getRoomId();
        Long memberId = talkAutoSaveEventDto.getMemberId();
        String msg = talkAutoSaveEventDto.getMsg();

        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
        talkHistoryRepository.save(talk);
    }

}
