package org.example.bikers.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.service.KakaoService;
import org.example.bikers.domain.member.service.NaverService;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final NaverService naverService;
    private final KakaoService kakaoService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoLogin(
        @RequestParam String code,
        HttpServletResponse response) {
        String token = kakaoService.login(code);

        return ResponseEntity.status(HttpStatus.OK)
            .header(JwtTokenProvider.AUTHORIZATION_HEADER, token).build();
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<Void> naverLogin(
        @RequestParam String code,
        @RequestParam String state) {
        String token = naverService.login(code, state);
        return ResponseEntity.status(HttpStatus.OK)
            .header(JwtTokenProvider.AUTHORIZATION_HEADER, token).build();
    }

}
