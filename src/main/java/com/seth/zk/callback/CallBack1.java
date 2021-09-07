package com.seth.zk.callback;

import org.apache.zookeeper.AsyncCallback;
import org.springframework.stereotype.Component;

/**
 * @Author Seth
 * @Date 2021/9/3
 */
@Component
public class CallBack1  implements AsyncCallback.StringCallback{
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("rc = " + rc);
        System.out.println("path = " + path);
        System.out.println("ctx = " + ctx);
        System.out.println("name = " + name);
    }
}
