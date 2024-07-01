package com.fastcampus.kafkahandson.ugc.postsearch;

import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;
import com.fastcampus.kafkahandson.ugc.port.PostSearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostSearchAdapter implements PostSearchPort {

    private final PostSearchRepository postSearchRepository;

    @Override
    public void indexPost(InspectedPost post) {
        postSearchRepository.save(PostDocumentConverter.toDocument(post));
    }

    @Override
    public void deletePost(Long id) {
        postSearchRepository.deleteById(id);
    }
}
