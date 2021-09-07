package com.seth.zk.config;

import com.seth.zk.watcher.DefaultWatcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author Seth
 * @Date 2021/9/2
 */
@Configuration
public class ZkConfig {
    @Bean
    public ZooKeeper zk(){
        ZooKeeper zooKeeper = null;
        CountDownLatch latch = new CountDownLatch(1);
        try {
            // 连接部署的zk集群
            DefaultWatcher watcher = new DefaultWatcher();
            zooKeeper = new ZooKeeper("10.13.9.234:2181,10.13.9.235:2181,10.13.9.236:2181/lock", 20000, watcher);
            watcher.setLatch(latch);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("zk connection error..");
        }
        return zooKeeper;
    }
}
