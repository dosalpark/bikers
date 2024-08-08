package org.example.bikers.domain.notice.entity;

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
@Table(name = "notification")
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String msg;

    @Column(nullable = false)
    private boolean isConfirm;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    public Notification(String sender, Long receiverId, String msg) {
        this.sender = sender;
        this.receiverId = receiverId;
        this.msg = msg;
        this.isConfirm = false;
        this.createdAt = LocalDateTime.now();
    }

}
