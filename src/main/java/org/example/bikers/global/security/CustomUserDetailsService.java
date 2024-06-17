package org.example.bikers.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.domain.member.entity.MemberStatus;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("회원정보 불일치"));
        if (member.getStatus().equals(MemberStatus.DELETE)) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다");
        }
        return new CustomUserDetails(member);
    }
}
