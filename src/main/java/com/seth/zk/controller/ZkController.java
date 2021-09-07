package com.seth.zk.controller;

import com.seth.zk.callback.CallBack1;
import com.seth.zk.util.LockUtil;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Author Seth
 * @Date 2021/9/2
 */
@RestController
public class ZkController {

    @GetMapping("/test")
    public void test(){
        for (int i = 0; i < 10; i++) {
            // 创建10个线程，观察是否按照节点顺序依次获得锁和释放锁
            new Thread(() -> {
                // 因为模拟的是分布式环境中的锁抢占情况，所以这里的对象都是自己创建的，包括类内部的其他对象。
                LockUtil lockUtil = new LockUtil();
                String threadName = Thread.currentThread().getName();
                lockUtil.setThreadName(threadName);
                lockUtil.tryLock();
                System.out.println("working...");
//                lockUtil.unlock();
            }).start();
        }
    }

    @Autowired
    private ZooKeeper zk;
    @Autowired
    private CallBack1 callBack1;

    AsyncCallback.DataCallback cb = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

        }
    };
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            Event.EventType type = event.getType();
            switch (type) {
                case None:
                    break;
                case NodeCreated:
                    break;
                case NodeDeleted:
                    break;
                case NodeDataChanged:
                    try {
                        byte[] callBack1s = zk.getData("/abc", true, new Stat());
                        System.out.println(new String(callBack1s));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case NodeChildrenChanged:
                    break;
                case DataWatchRemoved:
                    break;
                case ChildWatchRemoved:
                    break;
                case PersistentWatchRemoved:
                    break;
            }
            Event.KeeperState state = event.getState();
            switch (state) {
                case Unknown:
                    break;
                case Disconnected:
                    break;
                case NoSyncConnected:
                    break;
                case SyncConnected:
                    break;
                case AuthFailed:
                    break;
                case ConnectedReadOnly:
                    break;
                case SaslAuthenticated:
                    break;
                case Expired:
                    break;
                case Closed:
                    break;
            }
        }

    };

    @GetMapping("/abc")
    public void test1() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        List<String> children = zk.getChildren("/", false, stat);
        System.out.println(children.toString());
        zk.create("/abc", "world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, callBack1, "opq");

        zk.getData("/abc", watcher, cb, "this beautiful world！");
        zk.addWatch("/abc", watcher, AddWatchMode.PERSISTENT_RECURSIVE);
    }
}
