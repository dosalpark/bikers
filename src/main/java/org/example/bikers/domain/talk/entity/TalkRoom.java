package org.example.bikers.domain.talk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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
    private String roomId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long memberId;

    public TalkRoom(Long memberId, String name) {
        this.roomId = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.name = name;
    }

    public TalkRoom(Long memberId, String roomId, String name) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.name = name;
    }

}
