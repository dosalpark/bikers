package org.example.bikers.domain.post.service;

import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_POST;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.dto.PostGetResponseDto;
import org.example.bikers.domain.post.entity.Post;
import org.example.bikers.domain.post.entity.PostStatus;
import org.example.bikers.domain.post.repository.PostRepository;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(Long memberId, String title, String content) {
        Post newPost = new Post(memberId, title, content);
        postRepository.save(newPost);
    }

    @Transactional(readOnly = true)
    public PostGetResponseDto getPostById(Long postId) {
        Post getPost = postRepository.findPostByIdEqualsAndStatusNot(postId, PostStatus.DELETE)
            .orElseThrow(() -> new NotFoundException(NO_SUCH_POST));
        return converterDto(getPost);
    }

    private PostGetResponseDto converterDto(Post getPost) {
        return PostGetResponseDto.builder()
            .memberId(getPost.getMemberId())
            .title(getPost.getTitle())
            .content(getPost.getContent())
            .createdAt(getPost.getCreatedAt())
            .modifiedAt(getPost.getModifiedAt())
            .build();
    }

}
