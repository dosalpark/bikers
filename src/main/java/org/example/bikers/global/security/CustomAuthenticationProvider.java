//package org.example.bikers.global.security;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.example.bikers.domain.member.entity.Member;
//import org.example.bikers.domain.member.entity.MemberRole;
//import org.example.bikers.domain.member.repository.MemberRepository;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@RequiredArgsConstructor
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication)
//        throws AuthenticationException {
//        String email = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        System.out.println("CustomAuthenticationProvider");
//        Member member = memberRepository.findByEmail(email)
//            .orElseThrow(() -> new BadCredentialsException("해당하는 이용자 없음"));
//        if (!passwordEncoder.matches(password, member.getPassword())) {
////        if (password.equals(member.getPassword())) {
//            throw new BadCredentialsException("해당하는 이용자 없음");
//        }
//
////        UserDetails userDetails = new CustomUserDetails(member);
////        return new UsernamePasswordAuthenticationToken(userDetails, password,
////            userDetails.getAuthorities());
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(member.getMemberRole().getAuthority());
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
//        return new UsernamePasswordAuthenticationToken(member.getEmail(), password, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
