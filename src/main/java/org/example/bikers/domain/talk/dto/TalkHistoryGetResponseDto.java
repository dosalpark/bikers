package org.example.bikers.domain.talk.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TalkHistoryGetResponseDto {

    private Long talkId;
    private String memberEmail;
    private String msg;
    private LocalDateTime inputTime;

}
