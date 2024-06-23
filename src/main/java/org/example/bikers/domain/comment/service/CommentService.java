package org.example.bikers.domain.comment.service;

import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_COMMENT;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.comment.dto.CommentsGetResponseDto;
import org.example.bikers.domain.comment.entity.Comment;
import org.example.bikers.domain.comment.entity.CommentStatus;
import org.example.bikers.domain.comment.repository.CommentRepository;
import org.example.bikers.domain.post.service.PostService;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public void createComment(Long memberId, Long postId, String content) {
        postService.validateByPost(postId);
        Comment createComment = new Comment(memberId, postId, content);
        commentRepository.save(createComment);
    }

    @Transactional(readOnly = true)
    public List<CommentsGetResponseDto> getComments(Long postId) {
        postService.validateByPost(postId);
        List<Comment> getComments = commentRepository.findAllByPostIdEqualsOrderByCreatedAtAsc(
            postId);
        return converterToDtoList(getComments);
    }

    @Transactional
    public void updateComment(Long memberId, Long postId, Long commentId, String content) {
        postService.validateByPost(postId);
        Comment getComment = commentRepository.findCommentByIdEqualsAndStatusNot(commentId,
            CommentStatus.DELETE).orElseThrow(() -> new NotFoundException(NO_SUCH_COMMENT));
        validationCommentOwner(memberId, getComment.getMemberId());

        getComment.update(content);
        commentRepository.save(getComment);
    }

    private void validationCommentOwner(Long loginMemberId, Long commentOwnerId) {
        if (loginMemberId != commentOwnerId) {
            throw new IllegalArgumentException("작성자만 수정 및 삭제 할 수 있습니다.");
        }
    }

    private List<CommentsGetResponseDto> converterToDtoList(List<Comment> getComments) {
        List<CommentsGetResponseDto> responseDtoList = new ArrayList<>();

        for (Comment getComment : getComments) {
            String content = getComment.getStatus() == CommentStatus.DELETE
                ? "삭제된 댓글입니다." : getComment.getContent();
            CommentsGetResponseDto responseDto = CommentsGetResponseDto.builder()
                .commentId(getComment.getId())
                .memberId(getComment.getMemberId())
                .content(content)
                .createdAt(getComment.getCreatedAt())
                .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

}
