package org.example.bikers.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.dto.MemberDeleteRequestDto;
import org.example.bikers.domain.member.dto.MemberPromoteRequestDto;
import org.example.bikers.domain.member.dto.MemberSignupRequestDto;
import org.example.bikers.domain.member.dto.MemberUpdatePasswordRequestDto;
import org.example.bikers.domain.member.service.MemberService;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<Void> signUp(@Valid @RequestBody MemberSignupRequestDto requestDto) {
        memberService.singUp(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getCheckPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updateMemberByPassword(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody MemberUpdatePasswordRequestDto requestDto) {
        memberService.updateMemberByPassword(
            userDetails.getMember().getId(),
            requestDto.getOldPassword(),
            requestDto.getNewPassword(),
            requestDto.getChkNewPassword());
        return ResponseEntity.status((HttpStatus.OK)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody MemberDeleteRequestDto requestDto) {
        memberService.deleteMember(userDetails.getMember().getId(), requestDto.getDeleteMessage());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/promote")
    public ResponseEntity<Void> promoteToAdmin(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody MemberPromoteRequestDto requestDto) {
        memberService.promoteToAdmin(userDetails.getMember().getId(), requestDto.getSecretKey());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
