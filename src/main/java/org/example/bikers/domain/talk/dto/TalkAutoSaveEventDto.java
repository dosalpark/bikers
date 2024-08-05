package org.example.bikers.domain.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TalkAutoSaveEventDto {

    private Long roomId;
    private Long memberId;
    private String msg;

}
