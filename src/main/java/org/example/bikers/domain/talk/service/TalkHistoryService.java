package org.example.bikers.domain.talk.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.JoinTalkRoomHistoryEventDto;
import org.example.bikers.domain.talk.dto.LeaveTalkRoomHistoryEventDto;
import org.example.bikers.domain.talk.dto.TalkAutoSaveEventDto;
import org.example.bikers.domain.talk.dto.TalkHistoryGetResponseDto;
import org.example.bikers.domain.talk.entity.TalkHistory;
import org.example.bikers.domain.talk.repository.TalkHistoryRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class TalkHistoryService {

    private final TalkHistoryRepository talkHistoryRepository;
    private final TalkRoomMemberService talkRoomMemberService;

    @Transactional(readOnly = true)
    public List<TalkHistoryGetResponseDto> getTalkHistoryByRoomId(Long memberId, Long roomId) {
        talkRoomMemberService.validateMemberInTalkRoom(roomId, memberId);
        List<TalkHistoryGetResponseDto> responseDtoList =
            talkHistoryRepository.getTalkHistoryByRoomId(roomId);
        if (responseDtoList.isEmpty()) {
            throw new NotFoundException(ErrorCode.TALK_ROOM_EMPTY);
        }
        return responseDtoList;
    }


    @Transactional
    @EventListener
    public void wsTalkAutoSave(TalkAutoSaveEventDto talkAutoSaveEventDto) {
        Long roomId = talkAutoSaveEventDto.getRoomId();
        Long memberId = talkAutoSaveEventDto.getMemberId();
        String msg = talkAutoSaveEventDto.getMsg();

        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
        talkHistoryRepository.save(talk);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void joinTalkRoomNotice(JoinTalkRoomHistoryEventDto joinTalkRoomHistoryEventDto) {
        Long roomId = joinTalkRoomHistoryEventDto.getRoomId();
        Long memberId = joinTalkRoomHistoryEventDto.getMemberId();
        String msg = joinTalkRoomHistoryEventDto.getMsg();

        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
        talkHistoryRepository.save(talk);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void leaveTalkRoomNotice(LeaveTalkRoomHistoryEventDto leaveTalkRoomHistoryEventDto) {
        Long roomId = leaveTalkRoomHistoryEventDto.getRoomId();
        Long memberId = leaveTalkRoomHistoryEventDto.getMemberId();
        String msg = leaveTalkRoomHistoryEventDto.getMsg();

        TalkHistory talk = new TalkHistory(roomId, memberId, msg);
        talkHistoryRepository.save(talk);
    }

}
