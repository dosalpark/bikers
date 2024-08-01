package org.example.bikers.domain.talk.repository;

import org.example.bikers.domain.talk.entity.TalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRoomRepository extends JpaRepository<TalkRoom, Long> {

}
