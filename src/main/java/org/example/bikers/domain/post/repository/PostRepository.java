package org.example.bikers.domain.post.repository;

import java.util.Optional;
import org.example.bikers.domain.post.entity.Post;
import org.example.bikers.domain.post.entity.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostByIdEqualsAndStatusNot(Long postId, PostStatus status);

}
