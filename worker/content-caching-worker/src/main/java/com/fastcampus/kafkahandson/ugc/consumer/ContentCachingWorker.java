package com.fastcampus.kafkahandson.ugc.consumer;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.PostResolvingHelpUseCase;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.adapter.originpost.OriginalPostMessage;
import com.fastcampus.kafkahandson.ugc.adapter.originpost.OriginalPostMessageConverter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.fastcampus.kafkahandson.ugc.adapter.common.Topic.ORIGINAL_TOPIC;

@RequiredArgsConstructor
@Component
public class ContentCachingWorker {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final PostResolvingHelpUseCase postResolvingHelpUseCase;

    @KafkaListener(
            topics = {
                ORIGINAL_TOPIC
            },
            groupId = "cache-post-consumer-group",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message) {
        try {
            OriginalPostMessage originalPostMessage = objectMapper.readValue(message.value(), OriginalPostMessage.class);
            OperationType operationType = originalPostMessage.getOperationType();
            if (operationType == OperationType.CREATE) {
                this.handleCreate(originalPostMessage);
            } else if (operationType == OperationType.UPDATE) {
                this.handleUpdate(originalPostMessage);
            } else if (operationType == OperationType.DELETE) {
                this.handleDelete(originalPostMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCreate(OriginalPostMessage originalPostMessage) {
        postResolvingHelpUseCase.resolvedPostAndSave(OriginalPostMessageConverter.toModel(originalPostMessage));
    }

    private void handleUpdate(OriginalPostMessage originalPostMessage) {
        postResolvingHelpUseCase.resolvedPostAndSave(OriginalPostMessageConverter.toModel(originalPostMessage));
    }

    private void handleDelete(OriginalPostMessage originalPostMessage) {
        postResolvingHelpUseCase.deleteResolvedPost(originalPostMessage.getPayload().getId());
    }
}
