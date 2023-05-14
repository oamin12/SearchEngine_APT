package Crawler.src;

import com.mongodb.client.*;
import org.bson.Document;


public class DB {
    private MongoClient client = MongoClients.create("mongodb+srv://apt:project123@crawledcluster.r4jkkff.mongodb.net/?retryWrites=true&w=majority");
    private MongoDatabase database = client.getDatabase("sampleDB");
    private MongoCollection collection = database.getCollection("sampleCollection");

    public void insert(String url, String title, String body) {
        Document doc = new Document("url", url);
        doc.append("title", title);
        doc.append("body", body);
        collection.insertOne(doc);
    }

}