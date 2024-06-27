package org.example.bikers.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.service.OauthService;
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

    private final OauthService oauthService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoLogin(
        @RequestParam String code,
        HttpServletResponse response) {
        String token = oauthService.kakaoLogin(code);

        return ResponseEntity.status(HttpStatus.OK)
            .header(JwtTokenProvider.AUTHORIZATION_HEADER, token).build();
    }

}
