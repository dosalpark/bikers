package org.example.bikers.domain.talk.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TalkRoomGetResponseDto {

    private Long id;
    private String name;

}
