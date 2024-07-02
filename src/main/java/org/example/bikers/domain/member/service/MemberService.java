package org.example.bikers.domain.member.service;

import static org.example.bikers.global.exception.ErrorCode.DELETED_MEMBER;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_MEMBER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.member.entity.Member;
import org.example.bikers.domain.member.entity.MemberRole;
import org.example.bikers.domain.member.entity.MemberStatus;
import org.example.bikers.domain.member.entity.SignUpSource;
import org.example.bikers.domain.member.repository.MemberRepository;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.secret.key}")
    private String adminSecretKey;
    private static final String deleteMessageCheck = "회원탈퇴";

    @Transactional
    public void singUp(String email, String password, String checkPassword) {
        if (!password.equals(checkPassword)) {
            throw new IllegalArgumentException("패스워드 불일치");
        }
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new IllegalArgumentException("Email 중복");
        }
        Member newMember = new Member(email, passwordEncoder.encode(password), MemberRole.USER);
        memberRepository.save(newMember);
    }

    @Transactional
    public void updateMemberByPassword(Long memberId, String oldPassword, String newPassword,
        String chkNewPassword) {
        if (!newPassword.equals(chkNewPassword)) {
            throw new IllegalArgumentException("패스워드 불일치");
        }
        Member getMember = findByMember(memberId);
        if (!passwordEncoder.matches(oldPassword, getMember.getPassword())) {
            throw new IllegalArgumentException("패스워드 불일치");
        }

        getMember.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(getMember);
    }

    @Transactional
    public void deleteMember(Long memberId, String deleteMessage) {
        if (!deleteMessageCheck.equals(deleteMessage)) {
            throw new IllegalArgumentException("탈퇴하시려면 '회원탈퇴'를 정확히 입력해주세요");
        }
        Member getMember = findByMember(memberId);

        getMember.delete();
        memberRepository.save(getMember);
    }

    @Transactional
    public void promoteToAdmin(Long memberId, String secretKey) {
        if (!adminSecretKey.equals(secretKey)) {
            throw new IllegalArgumentException("secretKey 불일치");
        }
        Member getMember = findByMember(memberId);

        getMember.promote();
        memberRepository.save(getMember);
    }

    @Transactional
    public void validateByLocalUser(String email) {
        Member getMember = memberRepository.findByEmail(email).orElseThrow(() ->
            new NotFoundException(NO_SUCH_MEMBER));
        if (getMember.getStatus().equals(MemberStatus.DELETE)) {
            throw new NotFoundException(DELETED_MEMBER);
        }
        if (!getMember.getSignUpSource().equals(SignUpSource.LOCAL)) {
            throw new DuplicateKeyException(getMember.getSignUpSource() + " 플랫폼으로 가입한 회원입니다.");
        }
    }

    private Member findByMember(Long memberId) {
        Member getMember = memberRepository.findById(memberId).orElseThrow(() ->
            new NotFoundException(NO_SUCH_MEMBER));
        if (getMember.getStatus().equals(MemberStatus.DELETE)) {
            throw new NotFoundException(DELETED_MEMBER);
        }
        return getMember;
    }
}
