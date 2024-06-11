package org.example.bikers.global.config;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.example.bikers.global.provider.JwtTokenProvider;
import org.example.bikers.global.security.AuthenticationFilter;
import org.example.bikers.global.security.AuthorizationFilter;
import org.example.bikers.global.security.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    public final JwtTokenProvider jwtTokenProvider;
    public final MemberRepository memberRepository;
    public final CustomUserDetailsService userDetailsService;
    public final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(jwtTokenProvider, memberRepository);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(jwtTokenProvider);
        authenticationFilter.setAuthenticationManager(
            authenticationManager(authenticationConfiguration));
        return authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //CSRF 사용안함
        httpSecurity.csrf((csrf) -> csrf.disable());
        //세션 사용안함
        httpSecurity.sessionManagement(
            (sessionManagement) -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(
            (authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .requestMatchers("/v1").permitAll()
                .requestMatchers("/v1/members/**").permitAll()
                .anyRequest().authenticated()
        );
        httpSecurity.addFilterBefore(authorizationFilter(), AuthenticationFilter.class);
        httpSecurity.addFilterBefore(authenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
