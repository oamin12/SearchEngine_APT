package Crawler.src;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class DB {
    private final MongoClient client = MongoClients.create("mongodb+srv://apt:project123@crawledcluster.r4jkkff.mongodb.net/?retryWrites=true&w=majority");
    private final MongoDatabase database = client.getDatabase("sampleDB");
    private final MongoCollection collection = database.getCollection("trial");

    private final MongoClient crawlerClient = MongoClients.create("mongodb+srv://aptPlus:project123Plus@secondarycluster.ljixuac.mongodb.net/?retryWrites=true&w=majority");
    private final MongoDatabase crawlerDatabase = crawlerClient.getDatabase("crawlerDB");
    private final MongoCollection crawlerCollection = crawlerDatabase.getCollection("smallCrawler");
    private final MongoCollection PageRankConnections = crawlerDatabase.getCollection("PRConnections");
//    private final MongoCollection crawlerCollection2 = crawlerDatabase.getCollection("smallCrawler");
//    private final MongoCollection crawlerCollection3 = crawlerDatabase.getCollection("smallCrawler");

    public void insert(String url, String title, String body, String h1, String h2) {
        Document doc = new Document("url", url);
        doc.append("title", title);
        doc.append("body", body);
        doc.append("h1", h1);
        doc.append("h2", h2);
        crawlerCollection.insertOne(doc);
    }

    public void insertPRR(String url, String title, List<String> links) {
        Document doc = new Document("url", url);
        doc.append("title", title);
        doc.append("links", links);
        PageRankConnections.insertOne(doc);
    }

}