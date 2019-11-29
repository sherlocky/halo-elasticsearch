package com.sherlocky.halo.service.impl;

import com.github.pagehelper.PageHelper;
import com.sherlocky.halo.dao.PostDAO;
import com.sherlocky.halo.model.PostModel;
import com.sherlocky.halo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Halo 博客业务类
 * @author: zhangcx
 * @date: 2019/1/26 17:04
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDAO postDAO;

    /**
     * 根据id获取博文
     * @param id
     * @return
     */
    @Override
    public PostModel getPost(String id) {
        if (id == null) {
            return null;
        }
        return postDAO.get(id);
    }

    /**
     * 根据ids获取博文列表
     * @param ids
     * @return
     */
    @Override
    public List<PostModel> listPostsByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        return postDAO.listByIds(ids);
    }

    /**
     * 获取博文列表
     * @param pageNum  start from 1
     * @param pageSize
     * @return
     */
    @Override
    public List<PostModel> listPosts(int pageNum, int pageSize) {
        // 将参数传给这个方法就可以实现物理分页了，非常简单（页数从 1 开始）。
        PageHelper.startPage(pageNum, pageSize);
        return postDAO.list();
    }
}
