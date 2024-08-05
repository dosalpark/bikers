package org.example.bikers.domain.talk.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;
import org.example.bikers.domain.talk.entity.QTalkRoom;
import org.example.bikers.domain.talk.entity.QTalkRoomMember;

@RequiredArgsConstructor
public class TalkRoomMemberRepositoryImpl implements TalkRoomMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QTalkRoomMember talkRoomMember = QTalkRoomMember.talkRoomMember;
    private final QTalkRoom talkRoom = QTalkRoom.talkRoom;

    @Override
    public List<MyTalkRoomGetResponseDto> getMyRooms(Long memberId) {
        return queryFactory.select(
                Projections.constructor(MyTalkRoomGetResponseDto.class,
                    talkRoomMember.id,
                    talkRoomMember.roomId,
                    talkRoom.name)
            ).from(talkRoomMember)
            .leftJoin(talkRoom).on(talkRoomMember.roomId.eq(talkRoom.id))
            .where(
                talkRoomMember.joinMemberId.eq(memberId)
            )
            .orderBy(talkRoomMember.id.desc())
            .fetch();
    }
}
