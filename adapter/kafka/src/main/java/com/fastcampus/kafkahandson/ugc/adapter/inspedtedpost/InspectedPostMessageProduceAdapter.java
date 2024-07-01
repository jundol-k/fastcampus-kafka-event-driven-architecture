package com.fastcampus.kafkahandson.ugc.adapter.inspedtedpost;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.port.InspectedPostMessageProducePort;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.fastcampus.kafkahandson.ugc.adapter.common.Topic.INSPECTED_POST;

@RequiredArgsConstructor
@Service
public class InspectedPostMessageProduceAdapter implements InspectedPostMessageProducePort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();


    @Override
    public void sendCreateMessage(InspectedPost post) {
        InspectedPostMessage message = new InspectedPostMessage(
                post.getPost().getId(),
                OperationType.CREATE,
                new InspectedPostMessage.Payload(
                        post.getPost(),
                        post.getCategoryName(),
                        post.getAutoGeneratedTags(),
                        post.getInspectedAt()
                )
        );
        sendMessage(message);
    }

    @Override
    public void sendUpdateMessage(InspectedPost post) {
        InspectedPostMessage message = new InspectedPostMessage(
                post.getPost().getId(),
                OperationType.UPDATE,
                new InspectedPostMessage.Payload(
                        post.getPost(),
                        post.getCategoryName(),
                        post.getAutoGeneratedTags(),
                        post.getInspectedAt()
                )
        );
        sendMessage(message);
    }

    @Override
    public void sendDeleteMessage(Long id) {
        InspectedPostMessage message = new InspectedPostMessage(
                id,
                OperationType.DELETE,
                null
        );
        sendMessage(message);
    }

    private void sendMessage(InspectedPostMessage message) {
        try {
            kafkaTemplate.send(INSPECTED_POST, message.getId().toString(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}