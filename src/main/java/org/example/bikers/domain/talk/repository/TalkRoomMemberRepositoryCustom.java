package org.example.bikers.domain.talk.repository;

import java.util.List;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;

public interface TalkRoomMemberRepositoryCustom {

    List<MyTalkRoomGetResponseDto> getMyRooms(Long memberId);
}
