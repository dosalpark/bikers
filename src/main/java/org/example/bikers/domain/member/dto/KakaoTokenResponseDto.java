package org.example.bikers.domain.member.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoTokenResponseDto {

    @SerializedName("access_token")
    private String accessToken;

}
