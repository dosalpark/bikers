package org.example.bikers.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentsGetResponseDto {

    private Long commentId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;

}
