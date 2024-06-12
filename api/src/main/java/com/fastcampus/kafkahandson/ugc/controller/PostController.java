package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.PostCreateUseCase;
import com.fastcampus.kafkahandson.ugc.PostDeleteUseCase;
import com.fastcampus.kafkahandson.ugc.PostReadUseCase;
import com.fastcampus.kafkahandson.ugc.PostUpdateUseCase;
import com.fastcampus.kafkahandson.ugc.model.PostCreateRequest;
import com.fastcampus.kafkahandson.ugc.model.PostDetailDto;
import com.fastcampus.kafkahandson.ugc.model.PostDto;
import com.fastcampus.kafkahandson.ugc.model.PostUpdateRequest;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostReadUseCase postReadUseCase;
    private final PostCreateUseCase postCreateUseCase;
    private final PostUpdateUseCase postUpdateUseCase;
    private final PostDeleteUseCase postDeleteUseCase;
    private final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;

    @PostMapping
    ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequest request) {
        Post post = postCreateUseCase.create(
                new PostCreateUseCase.Request(
                        request.getUserId(),
                        request.getTitle(),
                        request.getContent(),
                        request.getCategoryId()
                )
        );

        return ResponseEntity.ok(toDto(post));
    }

    @PutMapping("/{postId}")
    ResponseEntity<PostDto> updatePost(@PathVariable("postId") Long id, @RequestBody PostUpdateRequest request) {
        Post post = postUpdateUseCase.update(
                new PostUpdateUseCase.Request(
                        id,
                        request.getTitle(),
                        request.getContent(),
                        request.getCategoryId()
                )
        );

        return ResponseEntity.ok(toDto(post));
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<PostDto> deletePost(@PathVariable("postId") Long id) {
        Post post = postDeleteUseCase.delete(new PostDeleteUseCase.Request(id));

        return ResponseEntity.ok(toDto(post));
    }

    private PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                post.getCategoryId(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }

    @GetMapping("/{postId}/detail")
    ResponseEntity<PostDetailDto> readPostDetail(@PathVariable("postId") Long id) {
        ResolvedPost resolvedPost = postReadUseCase.getById(id);

        return ResponseEntity.ok(toDto(resolvedPost));
    }

    private PostDetailDto toDto(ResolvedPost resolvedPost) {
        return new PostDetailDto(
                resolvedPost.getId(),
                resolvedPost.getTitle(),
                resolvedPost.getContent(),
                resolvedPost.getUserName(),
                resolvedPost.getCategoryName(),
                resolvedPost.getCreatedAt(),
                !resolvedPost.getCreatedAt().equals(resolvedPost.getUpdatedAt())
        );
    }
}
