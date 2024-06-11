package org.example.bikers.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.bikers.domain.member.dto.MemberLoginRequestDto;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;


    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/v1/members/login");
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            MemberLoginRequestDto requestDto = new ObjectMapper().readValue(
                request.getInputStream(),
                MemberLoginRequestDto.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                requestDto.getEmail(), requestDto.getPassword());

            setDetails(request, token);
            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain, Authentication authentication)
        throws IOException {
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        String token = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail());

        ObjectNode statusMsg = new ObjectMapper().createObjectNode();
        statusMsg.put("status", "200");
        statusMsg.put("meg", "success");
        String responseCode = new ObjectMapper().writeValueAsString(statusMsg);

        response.setStatus(200);
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
        response.setContentType("application/json");
        response.setContentLength(responseCode.length());
        response.getOutputStream().write(responseCode.getBytes());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        ObjectNode statusMsg = new ObjectMapper().createObjectNode();
        statusMsg.put("status", "401");
        statusMsg.put("meg", failed.getMessage());
        String responseCode = new ObjectMapper().writeValueAsString(statusMsg);

        response.setStatus(401);
        response.setContentType("application/json");
        response.setContentLength(responseCode.length());
        response.getOutputStream().write(responseCode.getBytes());
    }
}
