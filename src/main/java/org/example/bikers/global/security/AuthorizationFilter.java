package org.example.bikers.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthorizationFilter(JwtTokenProvider jwtTokenProvider,
        MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        //토큰가져와서
        String accessToken = jwtTokenProvider.getAccessTokenFromHeader(request);
        try {
            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {

                Claims info = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);
                Member member = memberRepository.findByEmailAndId(
                    info.get("email", String.class),
                    info.get("userId", Long.class)).orElseThrow();

                UserDetails userDetails = new CustomUserDetails(member);
                UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                CommonResponseDto.fail("400", "토큰이 유효하지 않습니다.")));
            return;
        } catch (ExpiredJwtException e) {
            String refreshToken = jwtTokenProvider.getRefreshTokenFromHeader(request);
            if (StringUtils.hasText(refreshToken)) {
                String memberInfo = jwtTokenProvider.getMemberInfoFromRefreshToken(refreshToken);
                if (StringUtils.hasText(memberInfo)) {
                    Long memberId = Long.valueOf(memberInfo.split(":")[0]);
                    String email = memberInfo.split(":")[1];

                    String newAccessToken = jwtTokenProvider.createAccessToken(memberId, email);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, newAccessToken);
                    response.getWriter().write(new ObjectMapper().writeValueAsString(
                        CommonResponseDto.success("200", "새로운 토큰이 발급되었습니다.")));
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                CommonResponseDto.fail("400", "토큰 만료 및 리프레시 토큰이 없습니다. 다시 로그인 해주세요.")));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
