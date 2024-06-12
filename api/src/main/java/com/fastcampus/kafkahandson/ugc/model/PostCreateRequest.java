package com.fastcampus.kafkahandson.ugc.model;

import lombok.Data;

@Data
public class PostCreateRequest {
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
}
