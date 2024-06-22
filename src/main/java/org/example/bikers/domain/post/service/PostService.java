package org.example.bikers.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.post.entity.Post;
import org.example.bikers.domain.post.repository.PostRepository;
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
}
