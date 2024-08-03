package org.example.bikers.domain.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveTalkRoomEventDto {

    private Long roomId;
    private Long memberId;
    private String msg;

}
