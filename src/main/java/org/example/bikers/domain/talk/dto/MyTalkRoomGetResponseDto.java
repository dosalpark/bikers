package org.example.bikers.domain.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyTalkRoomGetResponseDto {

    private Long id;
    private Long roomId;
    private String name;

}
