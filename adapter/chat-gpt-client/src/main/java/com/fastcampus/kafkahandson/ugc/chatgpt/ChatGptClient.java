package com.fastcampus.kafkahandson.ugc.chatgpt;


import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.chatgpt.model.ChatCompletionResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptClient {

    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    private static final String TARGET_GPT_MODEL = "gpt-3.5-turbo";

    private final WebClient chatGptWebClient;

    public ChatGptClient(@Qualifier("chatGptWebclient") WebClient chatGptWebClient) {
        this.chatGptWebClient = chatGptWebClient;
    }

    private final CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    public String getResultForContentWithPolicy(
            String content,
            ChatPolicy policy) {
        ChatCompletionResponse response = chatGptWebClient
                .post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openaiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "model", TARGET_GPT_MODEL,
                        "messages", List.of(
                                Map.of("role", "system", "content", policy.instruction),
                                Map.of("role", "user", "content", policy.exampleContent), // 대화의 쌍 중 1
                                Map.of("role", "assistant", "content", policy.exampleInspectionResult), // 대화의 쌍 중 2
                                Map.of("role", "user", "content", content)
                        ),
                        "stream", false
                ))
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .block();

        assert response != null;
        return response.getChoices()[0].getMessage().getContent();
    }

    @Data
    public static class ChatPolicy {
        private final String instruction;
        private final String exampleContent;
        private final String exampleInspectionResult;
    }
}
