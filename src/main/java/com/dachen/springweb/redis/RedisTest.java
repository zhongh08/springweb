package com.dachen.springweb.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RedisTest {

    private static Jedis jedis = RedisUtil.getJedis();

    public static void testString() {
        jedis.set("name","xinxin");
        System.out.println(jedis.get("name"));

        jedis.append("name", " is my lover");
        System.out.println(jedis.get("name"));

        jedis.del("name");
        System.out.println(jedis.get("name"));

        jedis.mset("name","liuling","age","23","qq","476777XXX");
        jedis.incr("age");
        System.out.println(jedis.get("name")  + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }

    public static void testMap() {
        // 添加数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("name","xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user",map);

        List<String> rsmap = jedis.hmget("user", "name", "age");
        System.out.println(rsmap);

        jedis.hdel("user", "age");
        System.out.println(jedis.hmget("user", "age")); //因为删除了，所以返回的是null
        System.out.println(jedis.hlen("user")); //返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true
        System.out.println(jedis.hkeys("user"));//返回map对象中的所有key
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
    }

    public static void testList() {
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework",0,-1));

        jedis.lpush("java framework","spring");
        jedis.lpush("java framework","struts");
        jedis.lpush("java framework","hibernate");

        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java framework",0,-1));

        jedis.del("java framework");
        jedis.rpush("java framework","spring");
        jedis.rpush("java framework","struts");
        jedis.rpush("java framework","hibernate");
        System.out.println(jedis.lrange("java framework",0,-1));
    }

    // 测试不使用管道
    public static void testInsert() {
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            jedis.set("test" + i, "test" + i);
        }
        long endTimeMillis = System.currentTimeMillis();
        System.out.println(endTimeMillis - currentTimeMillis);
    }

    // 测试管道
    public static void testPip() {
        long currentTimeMillis = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 1000; i++) {
            pipelined.set("bb" + i, i + "bb");
        }
        pipelined.sync();
        long endTimeMillis = System.currentTimeMillis();
        System.out.println(endTimeMillis - currentTimeMillis);
    }

    // 发布频道数据
    public static void testPubSub() {
        //发布频道 "ch1" 和消息 "hello redis"
        jedis.publish("ch1", "hello redis");
    }

    // 订阅频道数据
    public static void testSubscribe() {
        JedisPubSub jedisPubSub = new JedisPubSub() {

            // 当向监听的频道发送数据时，这个方法会被触发
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("收到消息" + message);
                //当收到 "unsubscribe" 消息时，调用取消订阅方法
                if ("unsubscribe".equals(message)) {
                    this.unsubscribe();
                }
            }

            // 当取消订阅指定频道的时候，这个方法会被触发
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                System.out.println("取消订阅频道" + channel);
            }

        };
        // 订阅之后，当前进程一致处于监听状态，当被取消订阅之后，当前进程会结束
        jedis.subscribe(jedisPubSub, "ch1");
    }

    public static void main(String[] args) {
        // redis存储字符串
        //testString();

        // redis操作Map
        //testMap();

        // jedis操作List
        //testList();

        //testInsert();
        //testPip();

        testPubSub();
        testSubscribe();

    }

}
