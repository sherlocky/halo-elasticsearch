package com.sherlocky.halo.constant;

import com.alibaba.druid.util.StringUtils;

/**
 * @author: zhangcx
 * @date: 2019/1/26 17:10
 */
public class HaloConstants {
    public static final String TABLENAME_POSTS = "posts";

    // 默认页起始(从0开始)
    public static final String PAGE_NO = "0";
    // 默认页大小
    public static final String PAGE_SIZE = "10";
    // ES 默认返回分页大小
    public static final Integer ES_PAGE_SIZE = 10;

    public abstract class OperatingType {
        public static final String INSERT = "i";
        public static final String UPDATE = "u";
        public static final String DELETE = "d";

        public boolean isInsert(String operatingType) {
            return StringUtils.equals(operatingType, INSERT);
        }

        public boolean isUpdate(String operatingType) {
            return StringUtils.equals(operatingType, UPDATE);
        }

        public boolean isDelete(String operatingType) {
            return StringUtils.equals(operatingType, DELETE);
        }
    }

    /** ES 博客数据更新时间 */
    public static Long BLOG_UPDATE_TIME = 0L;
}
