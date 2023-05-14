//this class will be used to process the word, remove stop words, and stem the word
package main.java.com.indexer;
import java.util.Dictionary;
import java.util.Hashtable;
import opennlp.tools.chunker.*;
import javax.sound.sampled.Port;

import java.util.ArrayList;
import java.util.Arrays;
import opennlp.tools.stemmer.PorterStemmer;


public class WordProcessor {
    PorterStemmer stemmer = new PorterStemmer();
    ArrayList<String> stopWords = new ArrayList<String>(Arrays.asList(
        "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
    "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below",
    "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't",
    "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from",
    "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
    "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his",
    "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't",
    "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself",
    "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our",
    "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll",
    "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the",
    "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they",
    "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too",
    "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're",
    "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's",
    "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would",
    "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself",
    "yourselves"));
    Dictionary<String, String> stemmedWords = new Hashtable<String, String>();
    public WordProcessor() {
        //this is the constructor
        
    }
    public String processWord(String word) {
        //this method will process the word
        //remove stop words
        //stem the word
        //return the word
        if (stopWords.contains(word)) {
            return null;
        }
        else {
            String stemmedWord = stemmer.stem(word);
            stemmedWords.put(word, stemmedWord);
            return stemmedWord;
        }
    }
    public Dictionary<String, String> getStemmedWords() {
        return stemmedWords;
    }
    //main to test the class
    public static void main(String[] args) {
        WordProcessor wp = new WordProcessor();
        String word = "testing";
        String stemmedWord = wp.processWord(word);
        System.out.println(stemmedWord);
    }

    
}
