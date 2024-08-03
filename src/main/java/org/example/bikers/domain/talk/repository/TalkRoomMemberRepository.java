package org.example.bikers.domain.talk.repository;

import java.util.Optional;
import org.example.bikers.domain.talk.entity.TalkRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRoomMemberRepository extends JpaRepository<TalkRoomMember, Long>,
    TalkRoomMemberRepositoryCustom {

    Optional<TalkRoomMember> findByRoomId(Long roomId);

    boolean existsByRoomIdAndJoinMemberId(Long roomId, Long memberId);

}
