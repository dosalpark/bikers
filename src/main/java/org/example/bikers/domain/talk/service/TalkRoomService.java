package org.example.bikers.domain.talk.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.CreateRoomEventDto;
import org.example.bikers.domain.talk.dto.TalkRoomGetResponseDto;
import org.example.bikers.domain.talk.entity.TalkRoom;
import org.example.bikers.domain.talk.repository.TalkRoomRepository;
import org.example.bikers.global.exception.ErrorCode;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalkRoomService {

    private final TalkRoomRepository talkRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createRoom(Long memberId, String name) {
        TalkRoom newRoom = new TalkRoom(memberId, name);
        talkRoomRepository.save(newRoom);

        eventPublisher.publishEvent(new CreateRoomEventDto(newRoom.getId(), memberId));
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
                .name(room.getName())
                .build())
            .toList();
    }

}
