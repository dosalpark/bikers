package org.example.bikers.domain.talk.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class TalkRoomCreateRequestDto {

    @NotEmpty
    private String name;

}
