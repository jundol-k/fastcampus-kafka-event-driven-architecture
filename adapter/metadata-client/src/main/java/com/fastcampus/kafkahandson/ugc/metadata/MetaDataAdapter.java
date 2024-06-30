package com.fastcampus.kafkahandson.ugc.metadata;

import com.fastcampus.kafkahandson.ugc.port.MetaDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MetaDataAdapter implements MetaDataPort {

    private final MetaDataClient metaDataClient;

    @Override
    public String getCategoryNameByCategoryId(Long categoryId) {
        MetaDataClient.CategoryResponse category = metaDataClient.getCategory(categoryId);
        if (category == null) return null;
        return category.getName();
    }

    @Override
    public String getUserNameByUserId(Long userId) {
        MetaDataClient.UserResponse user = metaDataClient.getUser(userId);
        if (user == null) return null;
        return user.getName();
    }

    @Override
    public List<Long> listFollowerIdsByUserId(Long userId) {
        return metaDataClient.getFollowersIdsByUserId(userId);
    }
}
