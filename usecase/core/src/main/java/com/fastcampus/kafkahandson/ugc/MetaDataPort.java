package com.fastcampus.kafkahandson.ugc;

import java.util.List;

public interface MetaDataPort {

    String getCategoryNameByCategoryId(Long categoryId);
    String getUserNameByUserId(Long userId);
    List<Long> listFollowerIdsByUserId(Long userId);
}
