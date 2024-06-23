package org.example.bikers.domain.comment.entity;

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

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    public Comment(Long memberId, Long postId, String content) {
        this.memberId = memberId;
        this.postId = postId;
        this.content = content;
        this.status = CommentStatus.NORMAL;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = CommentStatus.DELETE;
        this.modifiedAt = LocalDateTime.now();
    }

}
