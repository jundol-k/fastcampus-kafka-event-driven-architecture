package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.Data;

public interface PostUpdateUseCase {

    Post update(Request request);

    @Data
    class Request {
        private final Long userId;
        private final String title;
        private final String content;
        private final Long categoryId;
    }
}
