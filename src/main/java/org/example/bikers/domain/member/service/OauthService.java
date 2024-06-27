package org.example.bikers.domain.member.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.dto.OauthMemberResponseDto;
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
public class OauthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.key}")
    private String kakaoKey;
    @Value("${oauth.kakao.secret-key}")
    private String kakaoSecretKey;
    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public String kakaoLogin(String code) {
        String kakaoToken = getTokenByKakao(code);
        OauthMemberResponseDto responseDto = getMemberInfoByKakao(kakaoToken);
        Member getMember = registerKakaoUserIfNeeded(responseDto.getOauthId(),
            responseDto.getEmail());

        return jwtTokenProvider.createAccessToken(getMember.getId(), getMember.getEmail());
    }

    private String getTokenByKakao(String code) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .build()
            .toUri();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoKey);
        body.add("client_secret", kakaoSecretKey);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
            .post(uri)
            .headers(header)
            .body(body);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode()
            .is5xxServerError()) {
            throw new IllegalArgumentException("오류가 발생했습니다.");
        }
        JsonElement result = new JsonParser().parse(Objects.requireNonNull(response.getBody()));

        return result.getAsJsonObject().get("access_token").getAsString();
    }

    private OauthMemberResponseDto getMemberInfoByKakao(String token) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .build()
            .toUri();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        header.add("Authorization", "Bearer " + token);


        RequestEntity<String> request = new RequestEntity<>(header, HttpMethod.GET, uri);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JsonElement result = new JsonParser().parse(Objects.requireNonNull(response.getBody()));

        long oauthId = result.getAsJsonObject().get("id").getAsLong();
        JsonObject kakaoAccount = result.getAsJsonObject().get("kakao_account").getAsJsonObject();
        String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

        return OauthMemberResponseDto.builder()
            .oauthId(String.valueOf(oauthId))
            .email(email)
            .build();
    }

    private Member registerKakaoUserIfNeeded(String oauthId, String email) {
        Member getMemberByOauthId = memberRepository.findByOauthId(oauthId).orElse(null);
        if (getMemberByOauthId == null) {
            Member getMemberByEmail = memberRepository.findByEmail(email).orElse(null);
            if (getMemberByEmail == null) {
                Member newMember = new Member(
                    email,
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    MemberRole.USER,
                    oauthId,
                    SignUpSource.KAKAO);
                memberRepository.save(newMember);
                return newMember;
            } else {
                throw new DuplicateKeyException("동일한 이메일이 존재합니다. 이메일로 로그인해주세요");
            }
        }
        return getMemberByOauthId;
    }

}
