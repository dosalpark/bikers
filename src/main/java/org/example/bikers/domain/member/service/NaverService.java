package org.example.bikers.domain.member.service;

import com.google.gson.Gson;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.dto.NaverMemberInfoResponseDto;
import org.example.bikers.domain.member.dto.NaverResponseDto;
import org.example.bikers.domain.member.dto.OauthMemberResponseDto;
import org.example.bikers.domain.member.dto.OauthTokenResponseDto;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.domain.member.entity.MemberRole;
import org.example.bikers.domain.member.entity.SignUpSource;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class NaverService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${oauth.naver.key}")
    private String naverKey;
    @Value("${oauth.naver.secret-key}")
    private String naverSecretKey;
    @Value("${oauth.naver.redirect-uri}")
    private String naverRedirectUri;


    public String login(String code, String state) {
        String naverToken = getToken(code, state);
        OauthMemberResponseDto responseDto = getMemberInfo(naverToken);
        Member getMember = registerOAuthUserIfNeeded(responseDto.getOauthId(),
            responseDto.getEmail());

        return jwtTokenProvider.createAccessToken(getMember.getId(), getMember.getEmail());
    }


    private String getToken(String code, String state) {

        URI uri = UriComponentsBuilder
            .fromUriString("https://nid.naver.com")
            .path("/oauth2.0/token")
            .build()
            .toUri();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverKey);
        body.add("client_secret", naverSecretKey);
        body.add("code", code);
        body.add("state", state);

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
            .post(uri)
            .headers(header)
            .body(body);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode()
            .is5xxServerError()) {
            throw new IllegalArgumentException("오류가 발생했습니다.");
        }

        OauthTokenResponseDto responseDto = gson.fromJson(response.getBody(),
            OauthTokenResponseDto.class);

        return responseDto.getAccessToken();
    }

    private OauthMemberResponseDto getMemberInfo(String token) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/nid/me")
            .build()
            .toUri();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        header.add("Authorization", "Bearer " + token);

        RequestEntity<String> request = new RequestEntity<>(header, HttpMethod.GET, uri);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);



        NaverMemberInfoResponseDto memberInfoResponseDto = gson.fromJson(response.getBody(),
            NaverMemberInfoResponseDto.class);

        if (!memberInfoResponseDto.getMessage().equals("success")) {
            throw new IllegalArgumentException("에러");
        }

        NaverResponseDto responseDto = memberInfoResponseDto.getResponse();

        String oauthId = responseDto.getId();
        String email = responseDto.getEmail();

        return OauthMemberResponseDto.builder()
            .oauthId(String.valueOf(oauthId))
            .email(email)
            .build();

    }

    private Member registerOAuthUserIfNeeded(String oauthId, String email) {
        Member getMemberByOauthId = memberRepository.findByOauthId(oauthId).orElse(null);
        if (getMemberByOauthId == null) {
            Member getMemberByEmail = memberRepository.findByEmail(email).orElse(null);
            if (getMemberByEmail == null) {
                Member newMember = new Member(
                    email,
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    MemberRole.USER,
                    oauthId,
                    SignUpSource.NAVER);
                memberRepository.save(newMember);
                return newMember;
            } else {
                throw new DuplicateKeyException("동일한 이메일이 존재합니다. 이메일로 로그인해주세요");
            }
        }
        return getMemberByOauthId;
    }

}
