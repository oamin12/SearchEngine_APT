package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;


public class DB {
    private MongoClient client = MongoClients.create("mongodb+srv://apt:project123@crawledcluster.r4jkkff.mongodb.net/?retryWrites=true&w=majority");
    private MongoClient client2 = MongoClients.create("mongodb+srv://aptPlus:project123Plus@secondarycluster.ljixuac.mongodb.net/?retryWrites=true&w=majority");
    private MongoDatabase database = client.getDatabase("sampleDB");
    private MongoCollection collection = database.getCollection("sampleCollection");
    private MongoCollection indexedCollection = database.getCollection("indexedCollection");
    private MongoDatabase database2 = client2.getDatabase("indexer");
    private MongoCollection indexedCollectionUnstemmed = database2.getCollection("indexerUnstemmed");



    public void insert(String url, String title, String body) {
        Document doc = new Document("url", url);
        doc.append("title", title);
        doc.append("body", body);
        collection.insertOne(doc);
    }
    //this functions will return all the craweled documents in the database with their urls, titles, and bodies, and headers
    //for each 
    public MongoCursor<Document> getAll() {
        return collection.find().iterator();
    }
    //this function converts MongoCursor<Document> to array of CrawledOP objects
    public ArrayList<CrawlerOP> convertToArray(MongoCursor<Document> cursor) {
        ArrayList<CrawlerOP> arr = new ArrayList<CrawlerOP>();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            CrawlerOP op = new CrawlerOP();
            op.setUrl(doc.getString("url"));
            op.setTitle(doc.getString("title"));
            op.setBody(doc.getString("body"));
            op.setH1(doc.getString("h1"));
            op.setH2(doc.getString("h2"));
            arr.add(op);
        }
        return arr;
    }
    //this function takes the array of CrawledOP objects and calls indexer for each one then returns the array of IndexedWord objects
    public ArrayList<ArrayList<IndexedWord>> index(ArrayList<CrawlerOP> arr) {
        ArrayList<IndexedWord> indexed = new ArrayList<IndexedWord>();
        ArrayList<IndexedWord> indexedUnStemmed= new ArrayList<IndexedWord>();
        ArrayList<ArrayList<IndexedWord>> temp;
        ArrayList<IndexedWord> tempUnStemmed;
        ArrayList<IndexedWord> tempStemmed;
        
        indexer indexer = new indexer();
        for (CrawlerOP op : arr) {
            temp = indexer.index(op.getBody(),op.getUrl(),op.getTitle(),op.getH1(),op.getH2(),op.getTitle());
            tempStemmed = temp.get(0);
            tempUnStemmed = temp.get(1);
            for (IndexedWord word : tempStemmed) {
                indexed.add(word);
            }
            for (IndexedWord word : tempUnStemmed) {
                indexedUnStemmed.add(word);
            }

        }
        ArrayList<ArrayList<IndexedWord>> indexedArray = new ArrayList<ArrayList<IndexedWord>>();
        indexedArray.add(indexed);
        indexedArray.add(indexedUnStemmed);
        return indexedArray;
    }

    //this function takes the array of IndexedWord objects and inserts them in the database
    //each word has a lists of [url,Tf,Frequency ,inTitle, inH1, inH2, content]
    /*word:{
        {
            url: "www.google.com",
            tf: 0.5,
            frequency: 2,
            inTitle: true,
            inH1: false,
            inH2: true,
            content: "this is the content of the word"
        },
        {
            url: "www.google.com",
            tf: 0.5,
            frequency: 2,
            inTitle: true,
            inH1: false,
            inH2: true,
            content: "this is the content of the word"
        }
    }

    */
    public void insertIndexed(ArrayList<IndexedWord> indexed,ArrayList<IndexedWord> indexedUnStemmed) {
       
        List<WriteModel<Document>> updateModels = new ArrayList<>();
        List<WriteModel<Document>> updateModelsUnstemmed = new ArrayList<>();

        UpdateOptions options = new UpdateOptions().upsert(true);
        for (IndexedWord word : indexed) {
            //check if the word is already in the database
            // then add the new arraylist of urls to the old one
            // else insert the word with the arraylist of urls


            Document newList = new Document("url", word.getUrl());
            newList.append("frequency", word.getFrequency());
            newList.append("tf", word.getTf());
            newList.append("inTitle", word.getInTitle());
            newList.append("inH1", word.getInH1());
            newList.append("inH2", word.getInH2());
            newList.append("content", word.getContent());
            newList.append("title",word.getTitle());
            newList.append("indices", word.getIndices());
            
            


          
            updateModels.add(new UpdateOneModel<>(
                    Filters.eq("word", word.getWord()),
                    Updates.addToSet("urls", newList),
                    options
            ));

       }
         for (IndexedWord word : indexedUnStemmed) {
          //check if the word is already in the database
          // then add the new arraylist of urls to the old one
          // else insert the word with the arraylist of urls
          Document newList = new Document("url", word.getUrl());
          newList.append("frequency", word.getFrequency());
          newList.append("tf", word.getTf());
          newList.append("inTitle", word.getInTitle());
          newList.append("inH1", word.getInH1());
          newList.append("inH2", word.getInH2());
          newList.append("content", word.getContent());
          newList.append("title",word.getTitle());
          newList.append("indices", word.getIndices());
          
          updateModelsUnstemmed.add(new UpdateOneModel<>(
                  Filters.eq("word", word.getWord()),
                  Updates.addToSet("urls", newList),
                  options
          ));

     }
       if(!updateModels.isEmpty()) indexedCollection.bulkWrite(updateModels);
       
       if(!updateModelsUnstemmed.isEmpty()) indexedCollectionUnstemmed.bulkWrite(updateModelsUnstemmed);
    }

    //main function for testing
    public static void main(String[] args) {
        DB db = new DB();
        //db.insert("https://www.google.com", "Google", "Google is a search engine");
        MongoCursor<Document> cursor = db.getAll();
        //test convertToArray function
        ArrayList<CrawlerOP> arr = db.convertToArray(cursor);
        // for (CrawlerOP op : arr) {
        //     System.out.println(op.getUrl() + " " + op.getTitle() + " "  + " " + op.getH1() + " " + op.getH2());
        // }
        //test index function
        int count=0;
        //split data 3ashan el database byt3ml crash 3ashan el data kteer awi
        while(count<arr.size()){
            ArrayList<CrawlerOP> temp = new ArrayList<CrawlerOP>();
            for(int i=0;i<300;i++){
                temp.add(arr.get(count));
                count++;
            }
            ArrayList<ArrayList<IndexedWord>> indexedd = db.index(temp);
            ArrayList<IndexedWord> indexed = indexedd.get(0);
            ArrayList<IndexedWord> indexedUnStemmed = indexedd.get(1);
            db.insertIndexed(indexed,indexedUnStemmed);

        }
    //     ArrayList<IndexedWord> indexed = db.index(arr);
    //    for (IndexedWord word : indexed) {
    //        System.out.println(word.getWord() +  " " + word.getFrequency() + " " + word.getTf() + " " + word.getUrl() + " " + word.getInTitle() + " " + word.getInH1() + " " + word.getInH2() + " " + word.getContent());
    //    }
       //test insertIndexed function
        //db.insertIndexed(indexed);


        // //test getAll function
        // while (cursor.hasNext()) {

        //     System.out.println(cursor.next().toJson());
        // }

    }
}