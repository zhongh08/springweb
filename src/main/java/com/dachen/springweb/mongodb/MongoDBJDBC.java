package com.dachen.springweb.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBJDBC {

    public static void main(String[] args) {

        try {
            // 连接到mongodb服务
            MongoClient mongoClient = new MongoClient("192.168.2.100", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            // 创建集合
            /*
            mongoDatabase.createCollection("test");
            System.out.println("集合创建成功");
            */

            // 获取集合
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            // 插入文档
            /*
            Document document = new Document("title", "MongoDB")
                    .append("description", "database")
                    .append("likes",100)
                    .append("by", "Fly");
            List<Document> documents = new ArrayList<Document>();
            documents.add(document);
            collection.insertMany(documents);
            System.out.println("文档插入成功");
            */

            // 更新文档 将文档中likes=100修改为likes=200
            //collection.updateMany(Filters.eq("likes",100), new Document("$set", new Document("likes", 200)));

            // 删除符合条件的第一个文档
            //collection.deleteOne(Filters.eq("likes",200));
            // 删除所有符合条件的文档
            //collection.deleteMany(Filters.eq("likes",200));

            // 检索所有文档
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()){
                System.out.println(mongoCursor.next());
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }

    }

}
