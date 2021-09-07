package com.seth.zk.callback;

import com.seth.zk.util.LockUtil;
import lombok.Data;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @Author Seth
 * @Date 2021/9/3
 */
@Data
public class EphemeralSequentialNodeCreatedCallBack implements AsyncCallback.StringCallback {
    private String threadName;
    private ZooKeeper zk;
    private LockUtil lockUtil;
    private CountDownLatch latch;

    private LockChildrenCallBack lockChildrenCallBack = new LockChildrenCallBack();
    // 创建临时有序节点成功的回调
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            lockChildrenCallBack.setPathName("/" + name);
            lockChildrenCallBack.setThreadName(threadName);
            lockChildrenCallBack.setLatch(latch);
            lockUtil.setPath(name);
            System.out.println(threadName + " name = " + name);
            // 节点创建成功以后要获取根目录下的所有子节点
            zk.getChildren("/", false, lockChildrenCallBack, "");
        }
    }
}
