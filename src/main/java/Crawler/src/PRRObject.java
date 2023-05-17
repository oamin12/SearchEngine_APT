package Crawler.src;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PRRObject{
    String url;
    String title;
    HashSet<String> links;

    public PRRObject(String url, String title, HashSet<String> links){
        this.url = url;
        this.title = title;
        this.links = links;
    }

    public String getUrl(){
        return this.url;
    }

    public String getTitle(){
        return this.title;
    }

    public HashSet<String> getLinks(){
        return this.links;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setLinks(HashSet<String> links){
        this.links = links;
    }


}