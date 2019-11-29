package com.sherlocky.halo.service.impl;

import com.github.pagehelper.PageHelper;
import com.sherlocky.halo.dao.TrglogDAO;
import com.sherlocky.halo.model.TrglogModel;
import com.sherlocky.halo.service.TrglogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author: zhangcx
 * @date: 2019/1/26 18:15
 */
@Service
public class TrglogServiceImpl implements TrglogService {
    @Autowired
    private TrglogDAO trglogDAO;

    /**
     * 查询日志列表
     * @param tablename
     * @param operatingTypes
     * @param pageNum start from 1
     * @param pageSize
     * @return
     */
    @Override
    public List<TrglogModel> listTrglogs(String tablename, String[] operatingTypes, int pageNum, int pageSize) {
        if (StringUtils.isBlank(tablename)) {
            return Collections.EMPTY_LIST;
        }
        // 将参数传给这个方法就可以实现物理分页了，非常简单（页数从 1 开始）。
        PageHelper.startPage(pageNum, pageSize);
        return trglogDAO.listTrglogs(tablename, operatingTypes);
    }

    @Override
    public Long countTrglog(String tablename, String[] operatingTypes) {
        return trglogDAO.countTrglog(tablename, operatingTypes);
    }

    @Override
    public Long deleteTrglogs(Integer[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return 0L;
        }
        return trglogDAO.deleteTrglogs(ids);
    }

    @Override
    public Long deleteTrglog(Integer id) {
        if (id == null) {
            return 0L;
        }
        return trglogDAO.deleteTrglog(id);
    }

    @Override
    public Long deleteTrglogsBefore(String tablename, String[] guids, String[] operatingTypes, String datetime) {
        return trglogDAO.deleteTrglogsBefore(tablename, guids, operatingTypes, datetime);
    }
}
