package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;

import java.util.List;

public interface SubscribingPostPort {

    void addPostToFollowerInboxes(Post post, List<Long> followerUserIds);
    void removePostFromFollowerInboxes(Long postId);
    List<Long> listPostIdsByFollowerUserIdWithPagination(Long followerUserId, int page, int size);
}
