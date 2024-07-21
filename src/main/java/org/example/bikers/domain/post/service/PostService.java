package org.example.bikers.domain.post.service;

import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_POST;
import static org.example.bikers.global.exception.ErrorCode.POST_NOT_FOUND;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.dto.PostGetResponseDto;
import org.example.bikers.domain.post.dto.PostsGetResponseDto;
import org.example.bikers.domain.post.entity.Post;
import org.example.bikers.domain.post.entity.PostStatus;
import org.example.bikers.domain.post.repository.PostRepository;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private static final String DELETE_STATUS = "DELETE";

    @Transactional
    public void createPost(Long memberId, String title, String content) {
        Post newPost = new Post(memberId, title, content);
        postRepository.save(newPost);
    }

    @Transactional(readOnly = true)
    public PostGetResponseDto getPostById(Long postId) {
        PostGetResponseDto getPost = postRepository.getPost(postId, DELETE_STATUS);
        if (Objects.isNull(getPost)) {
            throw new NotFoundException(NO_SUCH_POST);
        }
        return getPost;
    }

    @Transactional(readOnly = true)
    public Slice<PostsGetResponseDto> getPosts(Pageable pageable) {
        Slice<PostsGetResponseDto> getPosts = postRepository.getPosts(pageable, DELETE_STATUS);
        if (getPosts.isEmpty()) {
            throw new NotFoundException(POST_NOT_FOUND);
        }
        return getPosts;
    }

    @Transactional
    public void updatePost(Long memberId, Long postId, String title, String content) {
        Post getPost = findByPost(postId);
        validationPostOwner(memberId, getPost.getMemberId());

        getPost.update(title, content);
        postRepository.save(getPost);
    }

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post getPost = findByPost(postId);
        validationPostOwner(memberId, getPost.getMemberId());

        getPost.delete();
        postRepository.save(getPost);
    }

    @Transactional(readOnly = true)
    public void validateByPost(Long postId) {
        postRepository.findPostByIdEqualsAndStatusNot(postId, PostStatus.DELETE)
            .orElseThrow(() -> new NotFoundException(NO_SUCH_POST));
    }

    private Post findByPost(Long postId) {
        return postRepository.findPostByIdEqualsAndStatusNot(postId, PostStatus.DELETE)
            .orElseThrow(() -> new NotFoundException(NO_SUCH_POST));
    }

    private void validationPostOwner(Long loginMemberId, Long postOwnerId) {
        if (loginMemberId != postOwnerId) {
            throw new IllegalArgumentException("작성자만 수정 및 삭제 할 수 있습니다.");
        }
    }

}
