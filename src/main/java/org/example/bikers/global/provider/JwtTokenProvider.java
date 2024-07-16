package org.example.bikers.global.provider;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.expire.access-token}")
    private long accessTokenExpireMilliSecond;

    @Value("${jwt.expire.refresh-token}")
    private long refreshTokenExpireMilliSecond;

    @Value("${jwt.secret.token}")
    private String secretToken;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretToken);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(Long userId, String email) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .setExpiration(new Date(date.getTime() + accessTokenExpireMilliSecond))
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    public String createRefreshToken(Long userId, String email) {
        Date date = new Date();
        String createRefreshToken = Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .setExpiration(new Date(date.getTime() + refreshTokenExpireMilliSecond))
            .signWith(key, signatureAlgorithm)
            .compact();

        redisTemplate.opsForValue().set(
            userId + ":" + email,
            createRefreshToken,
            Duration.ofMillis(refreshTokenExpireMilliSecond));

        return BEARER_PREFIX + createRefreshToken;
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
    }

    public Claims getUserInfoFromAccessToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateRefreshToken(String memberInfo) {
        String refreshToken = redisTemplate.opsForValue().get(memberInfo);
        return StringUtils.hasText(refreshToken);
    }

}
