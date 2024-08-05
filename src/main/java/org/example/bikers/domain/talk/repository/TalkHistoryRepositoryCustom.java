package org.example.bikers.domain.talk.repository;

import java.util.List;
import org.example.bikers.domain.talk.dto.TalkHistoryGetResponseDto;

public interface TalkHistoryRepositoryCustom {

    List<TalkHistoryGetResponseDto> getTalkHistoryByRoomId(Long roomId);

}
