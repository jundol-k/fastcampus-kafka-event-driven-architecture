package com.fastcampus.kafkahandson.ugc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribingPostRemoveFromInboxService implements SubscribingPostRemoveFromInboxUsecase {

    private final SubscribingPostPort subscribingPostPort;

    @Override
    public void deleteSubScribingInboxPost(Long postId) {
        subscribingPostPort.removePostFromFollowerInboxes(postId);
    }
}
