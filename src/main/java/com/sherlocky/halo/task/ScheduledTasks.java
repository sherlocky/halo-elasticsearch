package com.sherlocky.halo.task;

import com.sherlocky.halo.service.HaloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private HaloService haloService;
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 向 ElasticSearch 中同步博客数据
     */
    @Scheduled(cron = "0 30 7 * * ?") // prod 每天早上7点半执行
    /**
     * @Scheduled(cron = "0 0/1 * * * ?") // dev时每分钟触发一次
     * @Scheduled(fixedRate = 1000 * 3) // 固定间隔时间
     * @Scheduled(fixedDelay = 1000 * 3 ) // 固定等待时间
     */
    public void syncData2ES() {
        doSync();
    }

    public void doSync() {
        logger.info("### 同步博文到ES任务开始：当前时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
        logger.info("###### 保存触发的博文到ES开始~");
        haloService.storePost2Es();
        logger.info("###### 保存触发的博文到ES结束");
        logger.info("###### 从ES删除触发的博文开始~");
        haloService.removePostFromEs();
        logger.info("###### 从ES删除触发的博文结束");
        logger.info("### 同步博文到ES任务结束：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
    }
}