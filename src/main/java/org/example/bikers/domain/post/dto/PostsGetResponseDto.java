package org.example.bikers.domain.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostsGetResponseDto {

    private Long postId;
    private String title;
    private Long memberId;
    private LocalDateTime createdAt;

}
