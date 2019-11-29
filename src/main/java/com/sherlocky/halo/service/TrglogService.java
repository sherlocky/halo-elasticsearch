package com.sherlocky.halo.service;

import com.sherlocky.halo.model.TrglogModel;

import java.util.List;

/**
 * 触发器日志相关业务类
 * @author: zhangcx
 * @date: 2019/1/26 17:12
 */
public interface TrglogService {
    List<TrglogModel> listTrglogs(String tablename, String[] operatingTypes, int pageNum, int pageSize);

    Long countTrglog(String tablename, String[] operatingTypes);

    Long deleteTrglog(Integer id);

    Long deleteTrglogs(Integer[] ids);

    Long deleteTrglogsBefore(String tablename, String[] guids, String[] operatingTypes, String datetime);
}
