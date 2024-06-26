package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;
import com.fastcampus.kafkahandson.ugc.post.model.Post;

public interface PostInspectUsecase {

    // 검수결과가 정상이면 받는다.
    InspectedPost inspectAndGetIfValid(Post post);
}
