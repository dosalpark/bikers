package org.example.bikers.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberDeleteRequestDto {

    @NotEmpty(message = "탈퇴하시려면 위의 내용을 작성해주세요.")
    private String deleteMessage;

}
