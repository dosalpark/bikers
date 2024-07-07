package org.example.bikers.domain.member.dto;

import lombok.Getter;

@Getter
public class NaverMemberInfoResponseDto {
    private String resultcode;
    private String message;
    private NaverResponseDto response;

}
