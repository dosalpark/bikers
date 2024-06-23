package org.example.bikers.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentUpdateRequestDto {

    @NotEmpty(message = "댓글 내용을 입력해주세요")
    @Length(max = 50, message = "댓글은 50자 까지 작성가능합니다.")
    private String content;

}
