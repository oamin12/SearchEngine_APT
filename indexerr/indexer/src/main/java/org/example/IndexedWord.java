package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;

//this class is used to store the word and its frequency, TF, URL in the document
public class IndexedWord {

    String word;
    int frequency=0;
    double tf=0.0;
    String url;
    //array of indecies where the word is found in the document
    ArrayList<Integer> indecies = new ArrayList<Integer>();
    //string of content (4 words before and after the word)
    String content;

    public IndexedWord() {
        //this is the constructor
    }
    public String getWord() {
        return word;
    }
    public int getFrequency() {
        return frequency;
    }
    public double getTf() {
        return tf;
    }
    public String getUrl() {
        return url;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public void setTf(double tf) {
        this.tf = tf;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public ArrayList<Integer> getIndecies() {
        return indecies;
    }
    public void setIndecies(ArrayList<Integer> indecies) {
        this.indecies = indecies;
    }
    public void addIndex(int index) {
        indecies.add(index);
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    

}
