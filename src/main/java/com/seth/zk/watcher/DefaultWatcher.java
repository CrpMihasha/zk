package com.seth.zk.watcher;

import lombok.Data;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.util.concurrent.CountDownLatch;

/**
 * @Author Seth
 * @Date 2021/9/2
 */
@Data
public class DefaultWatcher implements Watcher {
    private ZooKeeper zk;
    private CountDownLatch latch;
    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.KeeperState state = watchedEvent.getState();
        switch (state) {
            case Unknown:
                break;
            case Disconnected:
                System.out.println("zk Disconnected...");
                break;
            case NoSyncConnected:
                System.out.println("zk NoSyncConnected...");
                break;
            case SyncConnected:
                latch.countDown();
                System.out.println("zk SyncConnected...");
                break;
            case AuthFailed:
                System.out.println("zk AuthFailed...");
                break;
            case ConnectedReadOnly:
                System.out.println("zk ConnectedReadOnly...");
                break;
            case SaslAuthenticated:
                System.out.println("zk SaslAuthenticated...");
                break;
            case Expired:
                System.out.println("zk Expired...");
                break;
            case Closed:
                System.out.println("zk Closed...");
                break;
        }

        Event.EventType type = watchedEvent.getType();
        switch (type) {
            case None:
                System.out.println("None");
                break;
            case NodeCreated:
                System.out.println("NodeCreated");
                break;
            case NodeDeleted:
                System.out.println("NodeDeleted");
                break;
            case NodeDataChanged:
                System.out.println("NodeDataChanged");
                break;
            case NodeChildrenChanged:
                System.out.println("NodeChildrenChanged");
                break;
            case DataWatchRemoved:
                System.out.println("DataWatchRemoved");
                break;
            case ChildWatchRemoved:
                System.out.println("ChildWatchRemoved");
                break;
            case PersistentWatchRemoved:
                System.out.println("PersistentWatchRemoved");
                break;
        }

    }
}
