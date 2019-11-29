package com.sherlocky.base;

import com.alibaba.fastjson.JSON;

/**
 * @author: zhangcx
 * @date: 2019/1/26 20:09
 */
public class BaseTest {
    public void beautiful(Object obj) {
        if (obj == null) {
            println("null");
            return;
        }
        if (obj instanceof String) {
            // println(obj);
            println(JSON.toJSONString(JSON.parseObject((String) obj), true));
            return;
        }
        // println(JSON.toJSONString(obj));
        println(JSON.toJSONString(obj, true));
    }

    public void println(Object obj) {
        System.out.println(obj);
    }
}
