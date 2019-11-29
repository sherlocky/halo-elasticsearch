package com.sherlocky.halo;

import com.sherlocky.halo.task.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HaloEsApplication implements CommandLineRunner {
    @Autowired
    private ScheduledTasks scheduledTasks;

    public static void main(String[] args) {
        /**
         * 【主要报错】
         * java.lang.IllegalStateException: availableProcessors is already set to [2], rejecting [2]
         * 【解决】
         * 初步判断为引入 spring data redis 后，redis 使用的 netty 和 Elasticsearch 使用的 netty 有冲突
         * So，在项目启动前设置一下相关属性，防止报错
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(HaloEsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动项目时 就同步一次
        scheduledTasks.doSync();
    }
}
