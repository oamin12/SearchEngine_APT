//this is the indexer class, it will be used to index the words in a webpage
//for each word, it will store the stemmed word, the url, and the frequency of the word in the webpage and idf
//for each url, it will create a dictionary of words and their frequency and idf in the webpage
//then the dictionary created will be added to the index saved in the database
package org.example;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class indexer {
    public indexer() {
        //this is the constructor
    }


    public ArrayList<IndexedWord> index(String documentContent,String url) {

        //this method will index the word
        //it will store the stemmed word, the url, and the frequency of the word in the webpage and idf
        //it will create a dictionary of words and their frequency and idf in the webpage
        //then the dictionary created will be added to the index saved in the database
        // the first entry in the array is the frequency and the rest are the indecies where the word is found in the document
        HashMap<String, ArrayList<Integer>> index = new HashMap<String, ArrayList<Integer>>();
        WordProcessor wp = new WordProcessor();
        String[] words = documentContent.split("[,!./ ]+");
        String[] wordsSpacePlitOnly=documentContent.split(" ");
        //loop through the words in the document content
        //if the word is not a stop word, stem it and add it to the index with count 1
        //if the word is already in the index, increment the count by 1
        for (int i = 0; i < words.length; i++) {
            String stemmedWord = wp.processWord(words[i]);
           
            if (stemmedWord != null) {
                 //remove (",',?,-,.,!,@,#,$,%,^,&,*,(,),{,},[,],/,<,>,:,;)
                stemmedWord = stemmedWord.replaceAll("[^a-zA-Z]", "");
                if (index.containsKey(stemmedWord)==false) {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    indecies.add(i);
                    index.put(stemmedWord, indecies);
                }
                else {
                    ArrayList<Integer> ListOfFrequencyIndecies = index.get(stemmedWord);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    ListOfFrequencyIndecies.add(i);
                    index.put(stemmedWord, ListOfFrequencyIndecies);
                }
            }
        }
        int totalWords = words.length;
        //create an object of the IndexedWord class
        //arraylist to store the indexed words
        ArrayList<IndexedWord> indexedWords = new ArrayList<IndexedWord>();
        //loop through the index and calculate the tf for each word
        //tf = frequency of the word / total number of words in the document
        for (String word : index.keySet()) {
            ArrayList<Integer> listS = index.get(word);
            int frequency = listS.get(0);
            double tf = (double)frequency / (double)totalWords;
            IndexedWord indexedWord = new IndexedWord();
            indexedWord.setWord(word);
            indexedWord.setFrequency(frequency);
            indexedWord.setTf(tf);
            indexedWord.setUrl(url);
            //remove the frequency from the list of indecies
            listS.remove(0);
            indexedWord.setIndecies(listS);
            indexedWords.add(indexedWord);
            //construct a sentence from the indecies of the word to add it to the content of the word
            String sentence = "";
            int FirstInstance = listS.get(0);
            //construct a word from the first instance of the word
            for(int i=-4;i<4;i++)
            {
                if(FirstInstance+i>=0 && FirstInstance+i<wordsSpacePlitOnly.length)
                {
                    sentence+=words[FirstInstance+i]+" ";
                }

            }
            indexedWord.setContent(sentence);


        }
        return indexedWords;
    }
    //document reader method
    //this method will read the document content from the file
    //it will return the document content as a string

    public static String readFileAsString(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

 

    //main to test the class
    public static void main(String[] args) {
        //test the class
         indexer indexer = new indexer();
         //read the document content from the file
         String documentContent = readFileAsString("D:\\University\\Senior 1\\2nd Term\\Advanced Programming Techniques\\Project\\SearchEngine_APT\\indexerr\\indexer\\src\\main\\java\\org\\example\\content.txt");
         //index the document content
         ArrayList<IndexedWord> indexedWords = indexer.index(documentContent,"https://www.google.com");
         //loop through the indexed words and print them
         for (IndexedWord indexedWord : indexedWords) {
             System.out.println(indexedWord.getWord() + " " + indexedWord.getFrequency() + " " + indexedWord.getTf() + " " + indexedWord.getUrl());
             //loop through the indecies of the word and print them
                for (int index : indexedWord.getIndecies()) {
                    System.out.print(index + " ");
                }
                System.out.println();
                //print the content of the word
                System.out.println(indexedWord.getContent());
                

         }

        
    }

    
}
