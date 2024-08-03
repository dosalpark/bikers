package org.example.bikers.domain.talk.repository;

import java.util.List;
import org.example.bikers.domain.talk.entity.TalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRoomRepository extends JpaRepository<TalkRoom, Long> {

    List<TalkRoom> findAllByNameContainsIgnoreCase(String roomName);

}
