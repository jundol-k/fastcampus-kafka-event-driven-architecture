package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import org.springframework.stereotype.Service;

@Service
public class PostDeleteService implements PostDeleteUseCase{
    @Override
    public Post delete(Request request) {
        return null;
    }
}
