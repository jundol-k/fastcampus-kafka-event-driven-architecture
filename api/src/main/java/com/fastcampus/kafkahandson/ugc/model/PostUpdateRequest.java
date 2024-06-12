package com.fastcampus.kafkahandson.ugc.model;

import lombok.Data;

@Data
public class PostUpdateRequest {
    private String title;
    private String content;
    private Long categoryId;
}
