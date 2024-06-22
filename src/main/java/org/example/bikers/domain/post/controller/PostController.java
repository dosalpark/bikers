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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<CommonResponseDto<Slice<PostsGetResponseDto>>> getPost(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String orderBy,
        @RequestParam(defaultValue = "desc") String direction) {

        if (page < 0) {
            page = 0;
        }
        if (size < 0) {
            size = 10;
        }
        if (!validationOrderBy(orderBy)) {
            orderBy = "createdAt";
        }
        if (!validationDirection(direction)) {
            direction = "desc";
        }

        Direction sortDirection = Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, sortDirection, orderBy);

        Slice<PostsGetResponseDto> responseDtoList = postService.getPost(pageable);

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

    private boolean validationOrderBy(String orderBy) {
        return orderBy.equals("createdAt") | orderBy.equals("modifiedAt");
    }

    private boolean validationDirection(String direction) {
        return direction.equals("asc") | direction.equals("desc");
    }

}
