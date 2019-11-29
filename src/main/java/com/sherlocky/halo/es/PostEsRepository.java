package com.sherlocky.halo.es;

import com.sherlocky.halo.model.PostModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author: zhangcx
 * @date: 2019/1/26 18:35
 */
@Component
public interface PostEsRepository extends ElasticsearchRepository<PostModel, String> {
    PostModel queryPostById(String id);
}
