package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.inspectedpost.AutoInspectionResult;
import com.fastcampus.kafkahandson.ugc.inspectedpost.InspectedPost;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class PostInspectService implements PostInspectUsecase {

    private final MetaDataPort metaDataPort;
    private final PostAutoInspectPort autoInspectPort;

    @Override
    public InspectedPost inspectAndGetIfValid(Post post) {
        String categoryName = metaDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        AutoInspectionResult inspectionResult = autoInspectPort.inspect(post, categoryName);
        if (!inspectionResult.getStatus().equals("GOOD")) return null;
        return InspectedPost.generate(
                post,
                categoryName,
                Arrays.asList(inspectionResult.getTags())
        );
    }
}
