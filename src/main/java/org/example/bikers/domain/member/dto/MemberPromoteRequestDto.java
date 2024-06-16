package org.example.bikers.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberPromoteRequestDto {

    @NotEmpty
    private String secretKey;

}
