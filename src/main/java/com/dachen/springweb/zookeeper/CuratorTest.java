package com.dachen.springweb.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

public class CuratorTest {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.2.100:2181", new RetryNTimes(10, 5000));
        client.start(); // 连接

        // 获取子节点，顺便监控子节点
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println("监控：" + event);
            }
        }).forPath("/");
        System.out.println(children);


        // Curator事件监听
        client.getCuratorListenable().addListener(new CuratorListener()
        {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception
            {
                System.out.println("事件： " + event);
            }
        });
        client.getConnectionStateListenable().addListener(new ConnectionStateListener()
        {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState)
            {
                System.out.println("连接状态事件： " + newState);
            }
        });
        client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener()
        {
            @Override
            public void unhandledError(String message, Throwable e)
            {
                System.out.println("错误事件：" + message);
            }
        });



        // 创建节点
        String result = client.create().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/test", "Data".getBytes());
        System.out.println(result);

        // 设置节点数据
        client.setData().forPath("/test","111".getBytes());
        client.setData().forPath("/test","222".getBytes());

        // 删除节点
        System.out.println(client.checkExists().forPath("/test"));
        client.delete().withVersion(-1).forPath("/test");
        System.out.println(client.checkExists().forPath("/test"));

        client.close();

        System.out.println("OK!");

    }

}
