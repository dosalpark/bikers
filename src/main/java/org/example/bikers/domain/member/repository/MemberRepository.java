package org.example.bikers.domain.member.repository;

import java.util.Optional;
import org.example.bikers.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndId(String email, Long id);

    Optional<Member> findByOauthId(String oauthId);
}
