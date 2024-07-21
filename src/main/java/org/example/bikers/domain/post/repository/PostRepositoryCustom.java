package org.example.bikers.domain.post.repository;

import org.example.bikers.domain.post.dto.PostsGetResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<PostsGetResponseDto> getPosts(Pageable pageable, String status);
}
