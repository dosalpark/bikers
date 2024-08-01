package org.example.bikers.domain.talk.repository;

import java.util.List;
import java.util.Optional;
import org.example.bikers.domain.talk.entity.TalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRoomRepository extends JpaRepository<TalkRoom, Long> {

    List<TalkRoom> findAllByMemberId(Long memberId);

    List<TalkRoom> findAllByNameContainsIgnoreCase(String roomName);

    boolean existsByRoomIdAndMemberId(String roomId, Long memberId);

    Optional<TalkRoom> findFirstByRoomId(String roomId);

}
