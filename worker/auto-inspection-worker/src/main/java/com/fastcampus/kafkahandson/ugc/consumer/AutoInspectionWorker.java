package com.fastcampus.kafkahandson.ugc.consumer;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.InspectedPostMessageProducePort;
import com.fastcampus.kafkahandson.ugc.PostInspectUsecase;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.adapter.common.Topic;
import com.fastcampus.kafkahandson.ugc.adapter.originpost.OriginalPostMessage;
import com.fastcampus.kafkahandson.ugc.adapter.originpost.OriginalPostMessageConverter;
import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AutoInspectionWorker {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final PostInspectUsecase postInspectUsecase;
    private final InspectedPostMessageProducePort inspectedPostMessageProducePort;

    @KafkaListener(
            topics = {Topic.ORIGINAL_TOPIC},
            groupId = "auto-inspection-consumer-group",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message) throws JsonProcessingException {
        OriginalPostMessage originalPostMessage = objectMapper.readValue(message.value(), OriginalPostMessage.class);
        if (originalPostMessage.getOperationType() == OperationType.CREATE) {
            handleCreate(originalPostMessage);
        } else if (originalPostMessage.getOperationType() == OperationType.UPDATE) {
            handleUpdate(originalPostMessage);
        } else if (originalPostMessage.getOperationType() == OperationType.DELETE) {
            handleDelete(originalPostMessage);
        }
    }

    private void handleCreate(OriginalPostMessage originalPostMessage) {
        InspectedPost inspectedPost = postInspectUsecase.inspectAndGetIfValid(
                OriginalPostMessageConverter.toModel(originalPostMessage)
        );
        if (inspectedPost == null) return;
        inspectedPostMessageProducePort.sendCreateMessage(inspectedPost);
    }
    private void handleUpdate(OriginalPostMessage originalPostMessage) {
        InspectedPost inspectedPost = postInspectUsecase.inspectAndGetIfValid(
                OriginalPostMessageConverter.toModel(originalPostMessage)
        );
        if (inspectedPost == null) {
            inspectedPostMessageProducePort.sendDeleteMessage(originalPostMessage.getPayload().getId());
        } else {
            inspectedPostMessageProducePort.sendUpdateMessage(inspectedPost);
        }
    }
    private void handleDelete(OriginalPostMessage originalPostMessage) {
        inspectedPostMessageProducePort.sendDeleteMessage(originalPostMessage.getId());
    }
}
