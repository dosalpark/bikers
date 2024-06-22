package org.example.bikers.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.dto.PostCreateRequestDto;
import org.example.bikers.domain.post.service.PostService;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody PostCreateRequestDto requestDto) {
        postService.createPost(
            userDetails.getMember().getId(),
            requestDto.getTitle(),
            requestDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
