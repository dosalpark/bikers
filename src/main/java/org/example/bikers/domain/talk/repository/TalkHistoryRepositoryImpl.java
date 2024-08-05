package org.example.bikers.domain.talk.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.entity.QMember;
import org.example.bikers.domain.talk.dto.TalkHistoryGetResponseDto;
import org.example.bikers.domain.talk.entity.QTalkHistory;

@RequiredArgsConstructor
public class TalkHistoryRepositoryImpl implements TalkHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QTalkHistory talkHistory = QTalkHistory.talkHistory;
    private final QMember member = QMember.member;


    @Override
    public List<TalkHistoryGetResponseDto> getTalkHistoryByRoomId(Long roomId) {
        return queryFactory.select(
                Projections.constructor(TalkHistoryGetResponseDto.class,
                    talkHistory.id,
                    member.email,
                    talkHistory.msg,
                    talkHistory.inputTime)
            ).from(talkHistory)
            .leftJoin(member).on(member.id.eq(talkHistory.memberId))
            .where(talkHistory.roomId.eq(roomId))
            .orderBy(talkHistory.id.desc())
            .fetch();
    }

}
