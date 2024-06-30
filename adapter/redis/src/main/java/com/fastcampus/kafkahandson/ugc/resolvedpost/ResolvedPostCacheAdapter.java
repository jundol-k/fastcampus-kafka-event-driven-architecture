package com.fastcampus.kafkahandson.ugc.resolvedpost;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.port.ResolvedPostCachePort;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ResolvedPostCacheAdapter implements ResolvedPostCachePort {

    private final String KEY_PREFIX = "resolved_post:v1:";
    private static final Long EXPIRE_SECONDS = 60L * 60L * 24L * 7L;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    @Override
    public void set(ResolvedPost resolvedPost) {
        String jsonString;
        try {
            jsonString = customObjectMapper.writeValueAsString(resolvedPost);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(generateCacheKey(resolvedPost.getId()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
    }

    @Override
    public ResolvedPost get(Long postId) {
        String value = redisTemplate.opsForValue().get(generateCacheKey(postId));
        if (value == null) return null;
        try {
            return customObjectMapper.readValue(value, ResolvedPost.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long postId) {
        redisTemplate.delete(generateCacheKey(postId));
    }

    @Override
    public List<ResolvedPost> multiGet(List<Long> postIds) {
        List<String> jsonStrings = redisTemplate.opsForValue().multiGet(postIds.stream().map(this::generateCacheKey).toList());
        if (jsonStrings == null) return List.of();
        return jsonStrings.stream().filter(Objects::nonNull).map(jsonString -> {
            try {
                return customObjectMapper.readValue(jsonString, ResolvedPost.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private String generateCacheKey(Long postId) {
        return KEY_PREFIX + postId;
    }
}
