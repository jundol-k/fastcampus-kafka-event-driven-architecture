package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribingPostAddToInboxService implements SubscribingPostAddToInboxUsecase {

    private final SubscribingPostPort subscribingPostPort;
    private final MetaDataPort metaDataPort;

    @Override
    public void saveSubscribingInBoxPost(Post post) {
        List<Long> followerUserIds = metaDataPort.listFollowerIdsByUserId(post.getUserId());
        subscribingPostPort.addPostToFollowerInboxes(post, followerUserIds);
    }
}
