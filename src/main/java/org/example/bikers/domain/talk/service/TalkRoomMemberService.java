package org.example.bikers.domain.talk.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;
import org.example.bikers.domain.talk.entity.TalkRoomMember;
import org.example.bikers.domain.talk.repository.TalkRoomMemberRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalkRoomMemberService {

    private final TalkRoomMemberRepository talkRoomMemberRepository;

    @Transactional(readOnly = true)
    public List<MyTalkRoomGetResponseDto> getMyRooms(Long memberId) {
        List<MyTalkRoomGetResponseDto> getMyRooms = talkRoomMemberRepository.getMyRooms(memberId);
        if (getMyRooms.isEmpty()) {
            throw new NotFoundException(ErrorCode.TALK_ROOM_NOT_FOUND);
        }
        return getMyRooms;
    }

    @Transactional
    public void joinRoom(Long memberId, Long roomId) {
        TalkRoomMember getRoom = talkRoomMemberRepository.findByRoomId(roomId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TALK_ROOM_NOT_FOUND));
        if (talkRoomMemberRepository.existsByRoomIdAndJoinMemberId(roomId, memberId)) {
            throw new IllegalArgumentException("이미 들어가있는 톡방입니다.");
        }
        TalkRoomMember joinTalkRoom = new TalkRoomMember(getRoom.getRoomId(), memberId);
        talkRoomMemberRepository.save(joinTalkRoom);
    }

    @Transactional(readOnly = true)
    public void validateMemberInTalkRoom(Long roomId, Long memberId) {
        if (!talkRoomMemberRepository.existsByRoomIdAndJoinMemberId(roomId, memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_IN_TALK_ROOM);
        }
    }
}
