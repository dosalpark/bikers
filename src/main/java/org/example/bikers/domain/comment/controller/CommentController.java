package org.example.bikers.domain.comment.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.comment.dto.CommentCreateResponseDto;
import org.example.bikers.domain.comment.dto.CommentUpdateRequestDto;
import org.example.bikers.domain.comment.dto.CommentsGetResponseDto;
import org.example.bikers.domain.comment.service.CommentService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long postId,
        @Valid @RequestBody CommentCreateResponseDto responseDto) {
        commentService.createComment(
            userDetails.getMember().getId(),
            postId,
            responseDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<CommentsGetResponseDto>>> getComments(
        @PathVariable Long postId) {
        List<CommentsGetResponseDto> responseDtoList = commentService.getComments(postId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @Valid @RequestBody CommentUpdateRequestDto requestDto) {
        commentService.updateComment(
            userDetails.getMember().getId(),
            postId,
            commentId,
            requestDto.getContent());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
