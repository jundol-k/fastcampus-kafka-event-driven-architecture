package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.SubscribingPostListUsecase;
import com.fastcampus.kafkahandson.ugc.model.PostListDto;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class PostListController {

    private final SubscribingPostListUsecase subscribingPostListUsecase;

    @GetMapping("/inbox/{userId}")
    ResponseEntity<List<PostListDto>> listSubscribingPosts(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page
    ) {
        List<ResolvedPost> resolvedPosts = subscribingPostListUsecase.listSubscribingInboxPosts(
                new SubscribingPostListUsecase.Request(page, userId)
        );
        return ResponseEntity.ok().body(resolvedPosts.stream().map(this::toDto).toList());
    }

    @GetMapping("/search")
    ResponseEntity<List<PostListDto>> searchPosts(
            @RequestParam("query") String query
    ) {
        return ResponseEntity.internalServerError().build();
    }

    private PostListDto toDto(ResolvedPost resolvedPost) {
        return new PostListDto(
                resolvedPost.getId(),
                resolvedPost.getTitle(),
                resolvedPost.getUserName(),
                resolvedPost.getCreatedAt()
        );
    }
}
