package org.example.bikers.domain.post.repository;

import org.example.bikers.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<Post> findAllPagable(Pageable pageable);

}
