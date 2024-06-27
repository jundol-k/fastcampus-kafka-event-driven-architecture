package com.fastcampus.kafkahandson.ugc.subscribingpost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SubscribingPostRepositoryImpl implements SubscribingPostCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<SubscribingPostDocument> findByFollowerUserIdWithPagination(Long followerUserId, int page, int size) {
        Query query = new Query()
                .addCriteria(Criteria.where("followerUserId").is(followerUserId))
                .with(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postCreatedAt"))
                );
        log.info(query.toString());
        return mongoTemplate.find(query, SubscribingPostDocument.class, "subscribingInboxPosts");
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        Query query = new Query()
                .addCriteria(Criteria.where("postId").is(postId));
        mongoTemplate.remove(query, SubscribingPostDocument.class, "subscribingInboxPosts");
    }
}
