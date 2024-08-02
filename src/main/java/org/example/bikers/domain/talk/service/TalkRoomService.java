package org.example.bikers.domain.talk.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;
import org.example.bikers.domain.talk.dto.TalkRoomGetResponseDto;
import org.example.bikers.domain.talk.entity.TalkRoom;
import org.example.bikers.domain.talk.repository.TalkRoomRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalkRoomService {

    private final TalkRoomRepository talkRoomRepository;

    @Transactional
    public void createRoom(Long memberId, String name) {
        TalkRoom newRoom = new TalkRoom(memberId, name);
        talkRoomRepository.save(newRoom);
    }

    @Transactional(readOnly = true)
    public List<MyTalkRoomGetResponseDto> getMyRooms(Long memberId) {
        List<TalkRoom> getMyRooms = talkRoomRepository.findAllByMemberId(memberId);
        if (getMyRooms.isEmpty()) {
            throw new NotFoundException(ErrorCode.TALK_ROOM_NOT_FOUND);
        }
        return getMyRooms.stream()
            .map(room -> MyTalkRoomGetResponseDto.builder()
                .id(room.getId())
                .roomId(room.getRoomId())
                .name(room.getName())
                .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TalkRoomGetResponseDto> getRooms(String roomName) {
        List<TalkRoom> getRooms = talkRoomRepository.findAllByNameContainsIgnoreCase(roomName);
        if (getRooms.isEmpty()) {
            throw new NotFoundException(ErrorCode.TALK_ROOM_NOT_FOUND);
        }
        return getRooms.stream()
            .map(room -> TalkRoomGetResponseDto.builder()
                .id(room.getId())
                .roomId(room.getRoomId())
                .name(room.getName())
                .build())
            .toList();
    }

    public void joinRoom(Long memberId, String roomId) {
        TalkRoom getRoom = talkRoomRepository.findFirstByRoomId(roomId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TALK_ROOM_NOT_FOUND));
        if (talkRoomRepository.existsByRoomIdAndMemberId(roomId, memberId)) {
            throw new IllegalArgumentException("이미 들어가있는 톡방입니다.");
        }
        TalkRoom joinTalkRoom = new TalkRoom(memberId, getRoom.getRoomId(), getRoom.getName());
        talkRoomRepository.save(joinTalkRoom);
    }

    public void validateMemberInTalkRoom(String roomId, Long memberId) {
        if (talkRoomRepository.existsByRoomIdAndMemberId(roomId, memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_IN_TALK_ROOM);
        }
    }

}
