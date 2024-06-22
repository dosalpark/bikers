package org.example.bikers.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotEmpty(message = "수정할 제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "수정할 내용을 입력해주세요")
    private String content;

}
