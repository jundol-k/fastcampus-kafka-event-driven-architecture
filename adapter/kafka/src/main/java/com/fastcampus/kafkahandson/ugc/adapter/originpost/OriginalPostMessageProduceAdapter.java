package com.fastcampus.kafkahandson.ugc.adapter.originpost;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.port.OriginalPostMessageProducePort;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.fastcampus.kafkahandson.ugc.adapter.common.Topic.ORIGINAL_TOPIC;

@RequiredArgsConstructor
@Service
public class OriginalPostMessageProduceAdapter implements OriginalPostMessageProducePort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @Override
    public void sendCreateMessage(Post post) {
        OriginalPostMessage originalPostMessage = convertToMessage(post.getId(), post, OperationType.CREATE);
        this.sendMessage(originalPostMessage);
    }

    @Override
    public void sendUpdateMessage(Post post) {
        OriginalPostMessage originalPostMessage = convertToMessage(post.getId(), post, OperationType.UPDATE);
        this.sendMessage(originalPostMessage);
    }

    @Override
    public void sendDeleteMessage(Long id) {
        OriginalPostMessage originalPostMessage = convertToMessage(id, null, OperationType.DELETE);
        this.sendMessage(originalPostMessage);
    }

    private OriginalPostMessage convertToMessage(Long id, Post post, OperationType operationType) {
        return new OriginalPostMessage(
                id,
                operationType,
                post == null ? null : new OriginalPostMessage.Payload(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUserId(),
                        post.getCategoryId(),
                        post.getCreatedAt(),
                        post.getUpdatedAt(),
                        post.getDeletedAt()
                )
        );
    }

    private void sendMessage(OriginalPostMessage message) {
        try {
            kafkaTemplate.send(ORIGINAL_TOPIC, message.getId().toString(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
