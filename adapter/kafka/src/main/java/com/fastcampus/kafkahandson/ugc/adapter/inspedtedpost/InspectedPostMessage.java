package com.fastcampus.kafkahandson.ugc.adapter.inspedtedpost;

import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InspectedPostMessage {
    private Long id;
    private OperationType operationType;
    private Payload payload;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Payload {
        private Post post;
        private String categoryName;
        private List<String> authGeneratedTags;
        private LocalDateTime inspectedAt;
    }
}
