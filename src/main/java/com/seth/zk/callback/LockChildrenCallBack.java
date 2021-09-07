package com.seth.zk.callback;

import lombok.Data;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author Seth
 * @Date 2021/9/3
 */
@Component
@Data
public class LockChildrenCallBack implements AsyncCallback.Children2Callback {
    private String threadName ;
    private String pathName ;
    private CountDownLatch latch;
    // 拿到根目录下所有子节点以后的回调
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("pathName = " + pathName);
        for (String child : children) {
            System.out.println(threadName + ", child: " + child);
        }
        // 对节点进行排序
        Collections.sort(children);
        // 判断当前节点是不是第一个节点，如果是
//        if ()
    }

}
