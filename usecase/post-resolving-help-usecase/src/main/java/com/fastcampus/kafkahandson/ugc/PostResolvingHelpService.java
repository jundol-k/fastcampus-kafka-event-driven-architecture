package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.port.MetaDataPort;
import com.fastcampus.kafkahandson.ugc.port.PostPort;
import com.fastcampus.kafkahandson.ugc.port.ResolvedPostCachePort;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostResolvingHelpService implements PostResolvingHelpUseCase{

    private final PostPort postPort;
    private final MetaDataPort metaDataPort;
    private final ResolvedPostCachePort resolvedPostCachePort;

    @Override
    public ResolvedPost resolvePostById(Long postId) {
        ResolvedPost resolvedPost = resolvedPostCachePort.get(postId);
        if (resolvedPost != null) return resolvedPost;
        Post post = postPort.findById(postId);
        return resolvePost(post);
    }

    @Override
    public List<ResolvedPost> resolvePostById(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) return List.of();
        List<ResolvedPost> resolvedPosts = new ArrayList<>();
        resolvedPosts.addAll(resolvedPostCachePort.multiGet(postIds)); // 유효기간이 지난 게시물은 없을 수 있음 캐시를 모두 믿을 수 없다.
        List<Long> missingPostIds = postIds.stream()
                .filter(postId -> resolvedPosts.stream().noneMatch(resolvedPost -> resolvedPost.getId().equals(postId)))
                .toList();
        if (!missingPostIds.isEmpty()) {
            List<Post> missingPosts = postPort.findByIds(missingPostIds);
            missingPosts.forEach(post -> {
                ResolvedPost resolvedPost = resolvePost(post);
                if (resolvedPost != null) resolvedPosts.add(resolvedPost);
            });
        }
        Map<Long, ResolvedPost> resolvedPostMap = resolvedPosts.stream()
                .collect(Collectors.toMap(ResolvedPost::getId, resolvedPost -> resolvedPost));
        return postIds.stream()
                .map(resolvedPostMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void resolvedPostAndSave(Post post) {
        ResolvedPost resolvedPost = resolvePost(post);
        if (resolvedPost != null) resolvedPostCachePort.set(resolvedPost);
    }

    @Override
    public void deleteResolvedPost(Long postId) {
        resolvedPostCachePort.delete(postId);
    }

    private ResolvedPost resolvePost(Post post) {
        if (post == null) return null;
        ResolvedPost resolvedPost = null;
        String userName = metaDataPort.getUserNameByUserId(post.getUserId());
        String categoryName = metaDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        if (userName != null && categoryName != null) {
            resolvedPost = ResolvedPost.generate(
                    post,
                    userName,
                    categoryName
            );
            resolvedPostCachePort.set(resolvedPost);
        }
        return resolvedPost;
    }
}
