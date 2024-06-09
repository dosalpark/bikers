package org.example.bikers.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        String token = jwtTokenProvider.getJwtFromHeader(request);
        try {
            if (Strings.hasText(token) && jwtTokenProvider.validateToken(token)) {

                Claims info = jwtTokenProvider.getUserInfoFromToken(token);
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
        } catch (SecurityException e) {
            response.sendError(401, "토큰 서명 검증 실패");
            return;
        } catch (MalformedJwtException e) {
            response.sendError(401, "토큰 형식 오류");
            return;
        } catch (ExpiredJwtException e) {
            response.sendError(401, "만료 토큰 사용");
            return;
        } catch (Exception e) {
            response.sendError(401, "인증 실패");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
