package com.sherlocky.halo.service;

import com.sherlocky.base.BaseTest;
import com.sherlocky.halo.es.PostEsService;
import com.sherlocky.halo.model.PostModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sherlocky.halo.constant.HaloConstants.ES_PAGE_SIZE;

/**
 * @author: zhangcx
 * @date: 2019/1/26 18:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostEsServiceTest extends BaseTest {
    @Autowired
    private PostEsService esService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostTrglogService postTrglogService;

    @Test
    public void testAdd2Es() {
        List<String> posts = postTrglogService.listNeedDeleteTriggerPostIds(1, 30);
        // beautiful(posts);
        beautiful(esService.addAll(postService.listPostsByIds(posts)));
        println("索引到 ES 成功！");
    }

    @Test
    public void testSearch() {
        beautiful(esService.search("转", 0, ES_PAGE_SIZE));
        println("查询测试成功！");
    }

    /**
     * 测试高亮搜索
     */
    @Test
    public void testHighlightSearch() {
        beautiful(esService.highlightSearch("分布式", 0, ES_PAGE_SIZE));
        println("高亮查询测试成功！");
    }

    // 测试删除ES中文档
    @Test
    public void testDeleteEsDoc() {
        esService.delete(Arrays.stream(new String[]{"101", "102", "103"}).map((id) -> {
            return new PostModel(id);
        }).collect(Collectors.toList()));
        println("删除成功！");
    }

    @Test
    public void testCountHighlightSearch() {
        println(esService.countHighlightSearch("分布式"));
    }
}
