package org.example.bikers.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "이메일 주소 형식을 지켜주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "영문,숫자를 이용하여 최소 4글자 최대 15글자까지 입력할 수 있습니다.")
    private String password;

    @NotBlank(message = "비밀번호를 한 번 더  입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "영문,숫자를 이용하여 최소 4글자 최대 15글자까지 입력할 수 있습니다.")
    private String checkPassword;

}
