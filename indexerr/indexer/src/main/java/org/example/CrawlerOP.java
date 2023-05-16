package org.example;
//this class is used to take the data from crawaler and send it to the indexer
public class CrawlerOP {
    String url;
    String body;
    String title;
    String h1;
    String h2;

    public CrawlerOP() {
        //this is the constructor
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getH2() {
        return h2;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

}
