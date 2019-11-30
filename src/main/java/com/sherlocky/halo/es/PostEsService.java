package com.sherlocky.halo.es;

import com.sherlocky.halo.model.PostModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * halo 博文 post ES 相关业务类
 *
 * @author: zhangcx
 * @date: 2019/1/26 18:36
 */
@Service
public class PostEsService {
    /**
     * 高亮的包裹标签配置
     */
    @Value("${sherlock.ES.highlight.preTag:<font color='#dd4b39'>}")
    private String preTag;
    @Value("${sherlock.ES.highlight.postTag:</font>}")
    private String postTag;
    /**
     * 搜索结果多匹配时的分隔字符串
     */
    @Value("${sherlock.ES.highlight.resultSeparator:<br>}")
    private String resultSeparator;
    @Autowired
    private PostEsRepository pr;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public PostModel add(PostModel pm) {
        return pr.save(pm);
    }

    public List<PostModel> addAll(List<PostModel> pms) {
        if (CollectionUtils.isEmpty(pms)) {
            return Collections.EMPTY_LIST;
        }
        return pms.stream().map((postModel -> {
            return pr.save(postModel);
        })).collect(Collectors.toList());
    }

    public void delete(String id) {
        pr.deleteById(id);
    }

    public void delete(Iterable<PostModel> pms) {
        pr.deleteAll(pms);
    }

    public void deleteAll() {
        pr.deleteAll();
    }

    public PostModel get(String id) {
        return pr.queryPostById(id);
    }

    /**
     * 简单搜索
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<PostModel> search(String keyword, int pageNo, int pageSize) {
        Page<PostModel> pms = pr.search(buildPostSearchQuery(keyword, pageNo, pageSize));
        return pms.get().collect(Collectors.toList());
    }

    /**
     * 构造 halo 博文 post 搜索 query
     * @param keyword 搜索关键字
     * @param pageNo
     * @param pageSize
     * @return
     */
    private SearchQuery buildPostSearchQuery(String keyword, int pageNo, int pageSize) {
        return buildPostSearchQuery(keyword, pageNo, pageSize, false);
    }

    /**
     * 构造 halo 博文 post 搜索 query
     * @param keyword 搜索关键字
     * @param pageNo
     * @param pageSize
     * @param highlight 是否高亮结果
     * @return
     */
    private SearchQuery buildPostSearchQuery(String keyword, int pageNo, int pageSize, boolean highlight) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
                // where
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                // order by 按照 分数 从高到低排列
                // SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
                // limit, 自定义分页,以每页10条划分
                .withPageable(PageRequest.of(pageNo, pageSize));
        // 是否需要高亮搜索结果
        if (highlight) {
            builder.withHighlightFields(
                    new HighlightBuilder.Field("content").preTags(preTag).postTags(postTag),
                    new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag));
        }
        return builder.build();
    }

    /**
     * 查询结果总数
     * @param keyword
     * @return
     */
    public long countHighlightSearch(String keyword) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
                // where
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"));
        return elasticsearchTemplate.count(builder.build(), PostModel.class);
    }

    /**
     * 结果带高亮的搜索
     *
     * @param keyword 搜索关键字
     * @param pageNo
     * @param pageSize
     * @return
     */
    public AggregatedPage<PostModel> highlightSearch(String keyword, int pageNo, int pageSize) {
        // 高亮字段
        AggregatedPage<PostModel> postModels = elasticsearchTemplate.queryForPage(buildPostSearchQuery(keyword, pageNo, pageSize, true), PostModel.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<PostModel> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    PostModel pm = new PostModel();
                    // 高亮结果中的 content 字段
                    HighlightField hlContent = searchHit.getHighlightFields().get("content");
                    if (hlContent != null) {
                        // 将多结果都收集起来
                        pm.setContent(StringUtils.join(hlContent.fragments(), resultSeparator));
                    }
                    HighlightField hlTitle = searchHit.getHighlightFields().get("title");
                    if (hlTitle != null) {
                        pm.setTitle(hlTitle.fragments()[0].toString());
                    } else {
                        // 即使title 中没有搜索到(高亮)值，也将 title 加到返回结果中
                        pm.setTitle((String) searchHit.getSourceAsMap().get("title"));
                    }
                    // id
                    pm.setId(searchHit.getId());
                    /* 从source中获取其他字段 */
                    // 地址 url
                    pm.setUrl((String) searchHit.getSourceAsMap().get("url"));
                    pm.setTags((String) searchHit.getSourceAsMap().get("tags"));
                    pm.setTagSlugs((String) searchHit.getSourceAsMap().get("tagSlugs"));
                    pm.setVisits((Integer) searchHit.getSourceAsMap().get("visits"));
                    pm.setPublishedAt((String) searchHit.getSourceAsMap().get("publishedAt"));
                    pm.setUpdatedAt((String) searchHit.getSourceAsMap().get("updatedAt"));
                    chunk.add(pm);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return null;
            }
        });
        return postModels;
    }
}
