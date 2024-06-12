package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.model.PostListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
public class PostListController {

    @GetMapping("/inbox/{userId}")
    ResponseEntity<List<PostListDto>> listSubscribingPosts(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/search")
    ResponseEntity<List<PostListDto>> searchPosts(
            @RequestParam("query") String query
    ) {
        return ResponseEntity.internalServerError().build();
    }
}
