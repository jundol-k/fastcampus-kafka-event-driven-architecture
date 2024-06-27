package com.fastcampus.kafkahandson.ugc.subscribingpost;

import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "subscribingInboxPosts")
@AllArgsConstructor
@Data
public class SubscribingPostDocument {
    @Id
    private String id; // postId + followerId
    private Long postId;
    private Long followerUserId;
    private LocalDateTime postCreatedAt;
    private LocalDateTime addedAt;
    private boolean read;

    public static SubscribingPostDocument generate(
            Post post,
            Long followerUserId
    ) {
        return new SubscribingPostDocument(
                generateDocumentId(post.getId(), followerUserId),
                post.getId(),
                followerUserId,
                post.getCreatedAt(),
                LocalDateTime.now(),
                false
        );
    }

    private static String generateDocumentId(Long postId, Long followerUserId) {
        return postId + "_" + followerUserId;
    }
}
