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
@Table(name = "talkHistory")
@NoArgsConstructor
public class TalkHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String msg;

    @Column(nullable = false)
    private LocalDateTime inputTime;

    public TalkHistory(Long roomId, Long memberId, String msg) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.msg = msg;
        this.inputTime = LocalDateTime.now();
    }

}
