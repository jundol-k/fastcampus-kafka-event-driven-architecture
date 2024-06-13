package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostResolvingHelpService implements PostResolvingHelpUseCase{

    private final PostPort postPort;
    private final MetaDataPort metaDataPort;

    @Override
    public ResolvedPost resolvePostById(Long postId) {
        ResolvedPost resolvedPost = null;
        Post post = postPort.findById(postId);
        if (post != null) {
            String userName = metaDataPort.getUserNameByUserId(post.getUserId());
            String categoryName = metaDataPort.getCategoryNameByCategoryId(post.getCategoryId());
            if (userName != null && categoryName != null) {
                resolvedPost = ResolvedPost.generate(
                        post,
                        userName,
                        categoryName
                );
            }
        }
        return resolvedPost;
    }

    @Override
    public List<ResolvedPost> resolvePostById(List<Long> postIds) {
        // TODO 임시이므로 수정필요
        return postIds.stream().map(this::resolvePostById).toList();
    }
}
