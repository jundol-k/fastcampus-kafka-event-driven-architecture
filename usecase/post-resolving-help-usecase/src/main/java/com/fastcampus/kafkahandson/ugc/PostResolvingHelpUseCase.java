package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;

import java.util.List;

public interface PostResolvingHelpUseCase {

    /**
     * 컨텐츠 상세
     * @param postId
     * @return
     */
    ResolvedPost resolvePostById(Long postId);

    /**
     * 컨텐츠 목록
     * @param postIds
     * @return
     */
    List<ResolvedPost> resolvePostById(List<Long> postIds);
}
