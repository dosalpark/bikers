package org.example.bikers.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.comment.dto.CommentCreateResponseDto;
import org.example.bikers.domain.comment.service.CommentService;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

}
