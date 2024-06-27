package org.example.bikers.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole;

    @Column
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SignUpSource signUpSource;

    @Column
    private String image;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    public Member(String email, String password, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.status = MemberStatus.NORMAL;
        this.memberRole = memberRole;
        this.signUpSource = SignUpSource.LOCAL;
        this.createdAt = LocalDateTime.now();
    }

    public Member(String email, String password, MemberRole memberRole, String oauthId,
        SignUpSource signUpSource) {
        this.email = email;
        this.password = password;
        this.status = MemberStatus.NORMAL;
        this.memberRole = memberRole;
        this.oauthId = oauthId;
        this.signUpSource = signUpSource;
        this.createdAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.modifiedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = MemberStatus.DELETE;
        this.modifiedAt = LocalDateTime.now();
    }

    public void promote() {
        this.memberRole = MemberRole.ADMIN;
        this.modifiedAt = LocalDateTime.now();
    }

}
