package com.dachen.springweb.elasticsearch;

import com.google.gson.JsonObject;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ElasticsearchTest {

    private final static String HOST = "192.168.2.100";
    private final static int PORT = 9300;

    public static void main(String[] args) {
        TransportClient client = null;

        try {
            // 创建客户端
            Settings settings = Settings.builder().put("client.transport.sniff", false).build();
            client = new PreBuiltTransportClient(settings).addTransportAddresses(
                    new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

            // 创建索引库
            /*IndexResponse response = client.prepareIndex("msg", "tweet", "1").setSource(XContentFactory.jsonBuilder()
                    .startObject().field("userName", "张三")
                    .field("sendDate", new Date())
                    .field("msg", "你好李四")
                    .endObject()).get();
            System.out.println("索引名称:" + response.getIndex() + "\n类型:" + response.getType()
                    + "\n文档ID:" + response.getId() + "\n当前实例状态:" + response.status());*/

            // 向索引库中添加json字符串
            /*String jsonStr = "{" +
                    "\"userName\":\"张三\"," +
                    "\"sendDate\":\"2017-11-30\"," +
                    "\"msg\":\"你好李四\"" +
                    "}";
            IndexResponse response = client.prepareIndex("weixin", "tweet").setSource(jsonStr,XContentType.JSON).get();
            System.out.println("json索引名称:" + response.getIndex() + "\njson类型:" + response.getType()
                    + "\njson文档ID:" + response.getId() + "\n当前实例json状态:" + response.status());*/

            // 向索引库添加一个Map集合
            /*Map<String, Object> map = new HashMap<String,Object>();
            map.put("userName", "张三");
            map.put("sendDate", new Date());
            map.put("msg", "你好李四");

            IndexResponse response = client.prepareIndex("momo", "tweet").setSource(map).get();
            System.out.println("map索引名称:" + response.getIndex() + "\n map类型:" + response.getType()
                    + "\n map文档ID:" + response.getId() + "\n当前实例map状态:" + response.status());*/

            // 向索引库添加JsonObject
            /*JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName", "张三");
            jsonObject.addProperty("sendDate", "2017-11-23");
            jsonObject.addProperty("msg","你好李四");

            IndexResponse response = client.prepareIndex("qq", "tweet").setSource(jsonObject, XContentType.JSON).get();
            System.out.println("jsonObject索引名称:" + response.getIndex() + "\n jsonObject类型:" + response.getType()
                    + "\n jsonObject文档ID:" + response.getId() + "\n当前实例jsonObject状态:" + response.status());*/

            // 从索引库获取数据
            /*GetResponse getResponse = client.prepareGet("msg", "tweet", "1").get();
            System.out.println("索引库的数据:" + getResponse.getSourceAsString());*/

            // 更新索引库数据
            /*JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName", "王五");
            jsonObject.addProperty("sendDate", "2008-08-08");
            jsonObject.addProperty("msg","你好,张三，好久不见");

            UpdateResponse updateResponse = client.prepareUpdate("msg", "tweet", "1")
                    .setDoc(jsonObject.toString(),XContentType.JSON).get();

            System.out.println("updateResponse索引名称:" + updateResponse.getIndex() + "\n updateResponse类型:" + updateResponse.getType()
                    + "\n updateResponse文档ID:" + updateResponse.getId() + "\n当前实例updateResponse状态:" + updateResponse.status());*/

            // 删除索引库的数据
            /*DeleteResponse deleteResponse = client.prepareDelete("msg", "tweet", "1").get();

            System.out.println("deleteResponse索引名称:" + deleteResponse.getIndex() + "\n deleteResponse类型:" + deleteResponse.getType()
                    + "\n deleteResponse文档ID:" + deleteResponse.getId() + "\n当前实例deleteResponse状态:" + deleteResponse.status());*/





            // 关闭客户端
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
