package org.example.bikers.domain.talk.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyTalkRoomGetResponseDto {

    private Long id;
    private String name;

}
