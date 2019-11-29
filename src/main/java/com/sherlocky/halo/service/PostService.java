package com.sherlocky.halo.service;

import com.sherlocky.halo.model.PostModel;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2019/1/26 17:12
 */
public interface PostService {
    PostModel getPost(String id);

    List<PostModel> listPostsByIds(List<String> ids);

    List<PostModel> listPosts(int pageNum, int pageSize);
}
