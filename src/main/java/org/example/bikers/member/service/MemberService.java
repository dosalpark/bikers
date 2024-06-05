package org.example.bikers.member.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bikers.member.entity.Member;
import org.example.bikers.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public String singUp(String email, String password, String checkPassword) {
        if (!password.equals(checkPassword)) {
            throw new IllegalArgumentException("패스워드 불일치");
        }
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new IllegalArgumentException("Email 중복");
        }
        Member newMember = new Member(email, password);
        memberRepository.save(newMember);

        return "success";
    }
}
