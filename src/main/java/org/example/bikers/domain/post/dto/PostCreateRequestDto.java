package org.example.bikers.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

}
