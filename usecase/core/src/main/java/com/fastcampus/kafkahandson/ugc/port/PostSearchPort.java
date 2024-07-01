package com.fastcampus.kafkahandson.ugc.port;

import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;

public interface PostSearchPort {
    void indexPost(InspectedPost post);
    void deletePost(Long id);
}
