package com.sherlocky.halo.service;

import com.sherlocky.halo.constant.HaloConstants;
import com.sherlocky.halo.es.PostEsService;
import com.sherlocky.halo.model.PostModel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.sherlocky.halo.constant.HaloConstants.ES_PAGE_SIZE;

/**
 * Halo 博客系统业务处理类
 *
 * @author: zhangcx
 * @date: 2019/1/28 15:04
 */
@Service
public class HaloService {
    private static Logger logger = LoggerFactory.getLogger(HaloService.class);
    @Autowired
    private PostTrglogService postTrglogService;
    @Autowired
    private PostEsService postEsService;
    @Autowired
    private PostService postService;

    /**
     * 保存触发的博文到ES
     */
    public void storePost2Es() {
        Date now = new Date();
        Long count = postTrglogService.countNeedStoreTriggerPosts();
        if (logger.isInfoEnabled()) {
            logger.info("### 有 {} 个需要触发【保存】的博文。", count);
        }
        List<String> postIds = new ArrayList<>();
        int page = (int) Math.ceil((double) count / ES_PAGE_SIZE);
        for (int pageNo = 1; pageNo <= page; pageNo++) {
            List<String> ids = postTrglogService.listNeedStoreTriggerPostIds(pageNo, ES_PAGE_SIZE);
            if (CollectionUtils.isEmpty(ids)) {
                continue;
            }
            postIds.addAll(ids);
            postEsService.addAll(postService.listPostsByIds(ids));
            postTrglogService.deleteStoredPostTrglogsBefore(ids.toArray(new String[0]), now);
        }
        // 简易更新时间，后续考虑持久化
        HaloConstants.BLOG_UPDATE_TIME = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info("### 更新时间：{}", HaloConstants.BLOG_UPDATE_TIME);
            logger.info("### 【保存】成功！");
        }
    }

    /**
     * 从ES删除触发的博文
     */
    public void removePostFromEs() {
        Date now = new Date();
        Long count = postTrglogService.countNeedDeleteTriggerPosts();
        if (logger.isInfoEnabled()) {
            logger.info("### 有 {} 个需要触发【删除】的博文。", count);
        }
        List<String> postIds = new ArrayList<>();
        int page = (int) Math.ceil((double) count / ES_PAGE_SIZE);
        if (logger.isInfoEnabled()) {
            logger.info("### {} 页", page);
        }
        for (int pageNo = 1; pageNo <= page; pageNo++) {
            List<String> ids = postTrglogService.listNeedDeleteTriggerPostIds(pageNo, ES_PAGE_SIZE);
            if (logger.isInfoEnabled()) {
                logger.info("### ids: {}", ids);
            }
            if (CollectionUtils.isEmpty(ids)) {
                continue;
            }
            postIds.addAll(ids);
            postEsService.delete(ids.stream().map((id) -> {
                return new PostModel(id);
            }).collect(Collectors.toList()));
            postTrglogService.deleteDeletedPostTrglogsBefore(ids.toArray(new String[0]), now);
        }
        // 简易更新时间，后续考虑持久化
        HaloConstants.BLOG_UPDATE_TIME = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info("### 更新时间：{}", HaloConstants.BLOG_UPDATE_TIME);
            logger.info("### 【删除】成功！");
        }
    }
}
