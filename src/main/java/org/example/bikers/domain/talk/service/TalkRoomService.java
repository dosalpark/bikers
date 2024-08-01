package org.example.bikers.domain.talk.service;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.entity.TalkRoom;
import org.example.bikers.domain.talk.repository.TalkRoomRepository;
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
}
