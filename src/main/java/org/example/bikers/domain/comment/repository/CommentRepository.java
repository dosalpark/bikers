package org.example.bikers.domain.comment.repository;

import java.util.List;
import org.example.bikers.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdEqualsOrderByCreatedAtAsc(Long postId);

}
