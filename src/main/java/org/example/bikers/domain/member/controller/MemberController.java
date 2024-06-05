package org.example.bikers.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.dto.MemberSignupRequestDto;
import org.example.bikers.domain.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signUp(@Valid @RequestBody MemberSignupRequestDto requestDto) {
        return memberService.singUp(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getCheckPassword());
    }
}
