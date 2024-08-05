package org.example.bikers.domain.talk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "talkRooms")
@NoArgsConstructor
public class TalkRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long memberId;

    @Column
    private LocalDateTime createdAt;

    public TalkRoom(Long memberId, String name) {
        this.name = name;
        this.memberId = memberId;
        this.createdAt = LocalDateTime.now();
    }

}
