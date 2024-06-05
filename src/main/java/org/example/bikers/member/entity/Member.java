package org.example.bikers.member.entity;

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

    @Column(nullable = false, length = 15)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    @Column
    private String image;

    @Column
    private LocalDateTime createdAt;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.status = MemberStatus.NORMAL;
        this.createdAt = LocalDateTime.now();
    }
}
