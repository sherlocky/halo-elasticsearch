package com.sherlocky.halo.service;

import com.sherlocky.base.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: zhangcx
 * @date: 2019/1/28 22:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HaloServiceTest extends BaseTest {
    private long startTime = 0;
    @Autowired
    private HaloService haloService;

    @Test
    public void testStorePost2Es() {
        haloService.storePost2Es();
    }

    @Test
    public void testRemovePostFromEs() {
        haloService.removePostFromEs();
    }

    @Before
    public void before() {
        startTime = System.currentTimeMillis();
    }
    @After
    public void after() {
        println("用时：" + ((System.currentTimeMillis() - startTime) / 1000) + " 秒");
    }
}
