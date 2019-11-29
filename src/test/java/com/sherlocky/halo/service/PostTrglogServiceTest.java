package com.sherlocky.halo.service;

import com.sherlocky.base.BaseTest;
import com.sherlocky.halo.constant.HaloConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.sherlocky.halo.constant.HaloConstants.TABLENAME_POSTS;

/**
 * @author: zhangcx
 * @date: 2019/1/26 17:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostTrglogServiceTest extends BaseTest {
    @Autowired
    private PostService postService;
    @Autowired
    private TrglogService trglogService;
    @Autowired
    private PostTrglogService postTrglogService;

    @Test
    public void testGetPost() {
        beautiful(postService.getPost("4"));
    }

    @Test
    public void testListPost() {
        int pageNo = 1;
        int pageSize = 10;
        beautiful(postService.listPosts(pageNo, pageSize));
    }

    @Test
    public void testListPostByIds() {
        List<String> ids = new ArrayList<>();
        ids.add("4");
        ids.add("5");
        ids.add("7");
        beautiful(postService.listPostsByIds(ids));
    }

    @Test
    public void testListTrglogs() {
        beautiful(trglogService.listTrglogs(TABLENAME_POSTS, new String[]{HaloConstants.OperatingType.UPDATE, HaloConstants.OperatingType.INSERT}, 1, 10));
    }

    @Test
    public void testCountTrglogs() {
        beautiful(trglogService.countTrglog(TABLENAME_POSTS, new String[]{HaloConstants.OperatingType.UPDATE, HaloConstants.OperatingType.INSERT}));
    }

    @Test
    public void testListTriggerPostIds() {
        // mybatis 分页插件 页数 是从1开始
        int pageNo = 1;
        int pageSize = 2;
        beautiful(postTrglogService.listNeedStoreTriggerPostIds(pageNo, pageSize));
        beautiful(postTrglogService.listNeedDeleteTriggerPostIds(pageNo, pageSize));

        pageNo = 0;
        beautiful(postTrglogService.listNeedStoreTriggerPostIds(pageNo, pageSize));
        beautiful(postTrglogService.listNeedDeleteTriggerPostIds(pageNo, pageSize));
    }
}
