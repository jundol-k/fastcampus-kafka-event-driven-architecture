package com.fastcampus.kafkahandson.ugc.consumer;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.PostIndexingUsecase;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.adapter.common.Topic;
import com.fastcampus.kafkahandson.ugc.adapter.inspedtedpost.InspectedPostMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ContentIndexingWorker {
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final PostIndexingUsecase postIndexingUsecase;

    @KafkaListener(
            topics = { Topic.INSPECTED_POST},
            groupId = "indexing-post-consumer-group",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message) {
        try {
            InspectedPostMessage inspectedPostMessage = objectMapper.readValue(message.value(), InspectedPostMessage.class);
            if (inspectedPostMessage.getOperationType() == OperationType.CREATE) {
                handleCreate(inspectedPostMessage);
            } else if (inspectedPostMessage.getOperationType() == OperationType.UPDATE) {
                handleUpdate(inspectedPostMessage);
            } else if (inspectedPostMessage.getOperationType() == OperationType.DELETE) {
                handleDelete(inspectedPostMessage);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleCreate(InspectedPostMessage inspectedPostMessage) {
        postIndexingUsecase.save(inspectedPostMessage.toModel());
    }

    private void handleUpdate(InspectedPostMessage inspectedPostMessage) {
        postIndexingUsecase.save(inspectedPostMessage.toModel());
    }

    private void handleDelete(InspectedPostMessage inspectedPostMessage) {
        postIndexingUsecase.delete(inspectedPostMessage.getId());
    }
}
