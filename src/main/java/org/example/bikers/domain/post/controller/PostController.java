package org.example.bikers.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.dto.PostCreateRequestDto;
import org.example.bikers.domain.post.dto.PostGetResponseDto;
import org.example.bikers.domain.post.dto.PostUpdateRequestDto;
import org.example.bikers.domain.post.dto.PostsGetResponseDto;
import org.example.bikers.domain.post.service.PostService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<PostGetResponseDto>> getPostById(
        @PathVariable Long postId) {
        PostGetResponseDto responseDto = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<Slice<PostsGetResponseDto>>> getPosts(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        Slice<PostsGetResponseDto> responseDtoList = postService.getPosts(pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long postId,
        @Valid @RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(
            userDetails.getMember().getId(),
            postId,
            requestDto.getTitle(),
            requestDto.getContent());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long postId) {
        postService.deletePost(
            userDetails.getMember().getId(),
            postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
