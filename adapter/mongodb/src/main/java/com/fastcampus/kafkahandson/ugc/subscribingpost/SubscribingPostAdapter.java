package com.fastcampus.kafkahandson.ugc.subscribingpost;

import com.fastcampus.kafkahandson.ugc.port.SubscribingPostPort;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribingPostAdapter implements SubscribingPostPort {

    private final SubscribingPostRepository subscribingPostRepository;

    @Override
    public void addPostToFollowerInboxes(Post post, List<Long> followerUserIds) {
        List<SubscribingPostDocument> documents = followerUserIds.stream()
                .map(followerUserId -> SubscribingPostDocument.generate(post, followerUserId))
                .toList();
        subscribingPostRepository.saveAll(documents);
    }

    @Override
    public void removePostFromFollowerInboxes(Long postId) {
        subscribingPostRepository.deleteAllByPostId(postId);
    }

    @Override
    public List<Long> listPostIdsByFollowerUserIdWithPagination(Long followerUserId, int page, int size) {
        return subscribingPostRepository.findByFollowerUserIdWithPagination(followerUserId, page, size)
                .stream()
                .map(SubscribingPostDocument::getPostId)
                .toList();
    }
}
