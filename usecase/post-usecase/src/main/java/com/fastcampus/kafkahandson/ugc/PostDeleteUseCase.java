package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.Data;

public interface PostDeleteUseCase {

    Post delete(Request request);

    @Data
    class Request{
        private final Long postId;
    }
}
