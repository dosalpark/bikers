package org.example.bikers.domain.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRoomEventDto {

    private Long roomId;
    private Long memberId;

}
