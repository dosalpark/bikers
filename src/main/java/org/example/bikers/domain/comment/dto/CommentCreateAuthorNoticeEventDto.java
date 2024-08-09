package org.example.bikers.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateAuthorNoticeEventDto {

    private Long authorId;
    private String postTitle;
    private String commentCreateEmail;

}
