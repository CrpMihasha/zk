package com.seth.zk.util;

import com.seth.zk.callback.EphemeralSequentialNodeCreatedCallBack;
import com.seth.zk.watcher.DefaultWatcher;
import lombok.Data;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import static org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

/**
 * @Author Seth
 * @Date 2021/9/2
 */
@Component
@Data
public class LockUtil{

    private EphemeralSequentialNodeCreatedCallBack ephemeralSequentialNodeCreatedCallBack;

    // 程序运行结束要清理这个对象
    private String threadName;

    private String path;

    private ZooKeeper zk;

    private CountDownLatch latch ;

    public LockUtil (){
        try {
            ephemeralSequentialNodeCreatedCallBack = new EphemeralSequentialNodeCreatedCallBack();
            DefaultWatcher watcher = new DefaultWatcher();
            latch = new CountDownLatch(1);
            zk = new ZooKeeper("10.13.9.234:2181,10.13.9.235:2181,10.13.9.236:2181/lock", 2000, watcher);
            watcher.setZk(zk);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用zk的临时有序节点来创建分布式锁，同一时间只有id最小的节点可以获得锁，后一个节点通过watch前一个节点来判断是否可以获得锁。
     * 只有当前一个节点释放锁时，后一个节点才可以获得锁，这样可以保证同一时间，分布式的系统中只有一个服务可以申请到锁。
     */
    public void tryLock(){
        ephemeralSequentialNodeCreatedCallBack.setThreadName(threadName);
        ephemeralSequentialNodeCreatedCallBack.setZk(zk);
        ephemeralSequentialNodeCreatedCallBack.setLockUtil(this);
        ephemeralSequentialNodeCreatedCallBack.setLatch(latch);
        zk.create("/lock",
                threadName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, // todo acl怎么使用
                EPHEMERAL_SEQUENTIAL,
                ephemeralSequentialNodeCreatedCallBack,
                "abc"); // todo 这个参数有什么含义
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 打印节点路径
        System.out.println("Thread: " + threadName+" get lock.");
    }

    public void unlock() {
        try {
            zk.delete(path, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

}
