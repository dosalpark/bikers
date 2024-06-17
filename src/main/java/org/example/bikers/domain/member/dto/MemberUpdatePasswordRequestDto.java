package org.example.bikers.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberUpdatePasswordRequestDto {

    @NotEmpty(message = "기존 패스워드를 입력해주세요")
    private String oldPassword;

    @NotEmpty(message = "변경할 패스워드를 입력해주세요")
    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "영문,숫자를 이용하여 최소 4글자 최대 15글자까지 입력할 수 있습니다.")
    private String newPassword;

    @NotEmpty(message = "변경할 패스워드를 입력해주세요")
    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "영문,숫자를 이용하여 최소 4글자 최대 15글자까지 입력할 수 있습니다.")
    private String chkNewPassword;
}
