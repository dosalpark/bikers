package org.example.bikers.domain.member.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoMemberInfoResponseDto {

    private long id;
    @SerializedName("kakao_account")
    private KakaoAccountResponseDto kakaoAccount;

}
