package com.sherlocky.halo.service;

import cn.hutool.core.date.DateTime;
import com.sherlocky.halo.constant.HaloConstants;
import com.sherlocky.halo.model.TrglogModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.sherlocky.halo.constant.HaloConstants.TABLENAME_POSTS;

/**
 * 博文触发相关业务类
 * @author: zhangcx
 * @date: 2019/1/26 18:47
 */
@Service
public class PostTrglogService {
    @Autowired
    private PostService postService;
    @Autowired
    private TrglogService trglogService;
    private String[] needStoreOps = new String[]{HaloConstants.OperatingType.INSERT, HaloConstants.OperatingType.UPDATE};
    private String[] needDeleteOps = new String[]{HaloConstants.OperatingType.DELETE};

    /**
     * 列出触发的 需要存储的 博文s
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<String> listNeedStoreTriggerPostIds(int pageNum, int pageSize) {
        return this.listNeedOperateTriggerPostIds(needStoreOps, pageNum, pageSize);
    }

    /**
     * 列出触发的 需要删除的 博文s
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<String> listNeedDeleteTriggerPostIds(int pageNum, int pageSize) {
        return this.listNeedOperateTriggerPostIds(needDeleteOps, pageNum, pageSize);
    }

    /**
     * 查询触发的 需要操作（存储/删除）的 博文s
     * @param pageNum
     * @param pageSize
     * @return
     */
    private List<String> listNeedOperateTriggerPostIds(String[] operatingTypes, int pageNum, int pageSize) {
        List<TrglogModel> logs = trglogService.listTrglogs(TABLENAME_POSTS, operatingTypes, pageNum, pageSize);
        if (CollectionUtils.isEmpty(logs)) {
            return Collections.EMPTY_LIST;
        }
        return logs.stream().map((trglogModel) -> {
            return trglogModel.getGuid();
        }).distinct().collect(Collectors.toList());
    }

    /**
     * 统计需要存储的 博文 触发数
     * @return
     */
    public Long countNeedStoreTriggerPosts() {
        return trglogService.countTrglog(TABLENAME_POSTS, needStoreOps);
    }

    /**
     * 统计需要删除的 博文 触发数
     * @return
     */
    public Long countNeedDeleteTriggerPosts() {
        return trglogService.countTrglog(TABLENAME_POSTS, needDeleteOps);
    }

    /**
     * 日志消费掉以后，应该删掉相关的 所有(旧)日志
     * @param guids
     * @param datetime
     * @param datetime
     * @return
     */
    public Long deleteStoredPostTrglogsBefore(String[] guids, Date datetime) {
        return trglogService.deleteTrglogsBefore(TABLENAME_POSTS, guids, needStoreOps, DateTime.of(datetime).toString());
    }

    public Long deleteDeletedPostTrglogsBefore(String[] guids, Date datetime) {
        return trglogService.deleteTrglogsBefore(TABLENAME_POSTS, guids, needDeleteOps, DateTime.of(datetime).toString());
    }
}
