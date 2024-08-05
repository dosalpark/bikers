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

@Getter
@Entity
@Table(name = "talkRoomMembers")
@NoArgsConstructor
public class TalkRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private Long joinMemberId;

    @Column(nullable = false)
    private LocalDateTime joinTime;

    public TalkRoomMember(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.joinMemberId = memberId;
        this.joinTime = LocalDateTime.now();
    }

}
