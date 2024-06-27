package org.example.bikers.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthMemberResponseDto {

    private String oauthId;
    private String email;

}
