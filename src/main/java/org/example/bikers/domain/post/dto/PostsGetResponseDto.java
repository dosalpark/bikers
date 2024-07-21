package org.example.bikers.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsGetResponseDto {

    private Long postId;
    private String title;
    private String email;
    private Long commentCount;
    private LocalDateTime createdAt;

}
