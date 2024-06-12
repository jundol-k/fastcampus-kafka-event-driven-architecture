package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;

public interface PostReadUseCase {

    ResolvedPost getById(Long id);
}
