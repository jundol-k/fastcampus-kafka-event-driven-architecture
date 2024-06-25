package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostDeleteService implements PostDeleteUseCase{

    private final PostPort postPort;
    private final OriginalPostMessageProducePort originalPostMessageProducePort;

    @Transactional
    @Override
    public Post delete(Request request) {
        Post post = postPort.findById(request.getPostId());
        if (post == null) return null;
        post.delete();
        Post deletedPost = postPort.save(post); // soft Delete (deletedAt을 채운다)
        originalPostMessageProducePort.sendDeleteMessage(post.getId());
        return deletedPost;
    }
}
