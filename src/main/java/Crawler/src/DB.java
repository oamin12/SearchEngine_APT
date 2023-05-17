package Crawler.src;

import com.mongodb.client.*;
import org.bson.Document;


public class DB {
    private final MongoClient client = MongoClients.create("mongodb+srv://apt:project123@crawledcluster.r4jkkff.mongodb.net/?retryWrites=true&w=majority");
    private final MongoDatabase database = client.getDatabase("sampleDB");
    private final MongoCollection collection = database.getCollection("trial");

    private final MongoClient crawlerClient = MongoClients.create("mongodb+srv://aptPlus:project123Plus@secondarycluster.ljixuac.mongodb.net/?retryWrites=true&w=majority");
    private final MongoDatabase crawlerDatabase = crawlerClient.getDatabase("crawlerDB");
    private final MongoCollection crawlerCollection = crawlerDatabase.getCollection("smallCrawler");

    public void insert(String url, String title, String body, String h1, String h2) {
        Document doc = new Document("url", url);
        doc.append("title", title);
        doc.append("body", body);
        doc.append("h1", h1);
        doc.append("h2", h2);
        collection.insertOne(doc);
    }

}