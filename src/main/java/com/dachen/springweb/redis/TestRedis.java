package com.dachen.springweb.redis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestRedis {

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

    public static void main(String[] args) {
        // redis存储字符串
        //testString();

        // redis操作Map
        //testMap();

        // jedis操作List
        //testList();
    }

}
