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
import java.lang.reflect.Array;

public class indexer {
    public indexer() {
        //this is the constructor
    }


    public ArrayList<ArrayList<IndexedWord>> index(String documentContent,String url,String title,String h1,String h2,String Linktitle) {

        //this method will index the word
        //it will store the stemmed word, the url, and the frequency of the word in the webpage and idf
        //it will create a dictionary of words and their frequency and idf in the webpage
        //then the dictionary created will be added to the index saved in the database
        // the first entry in the array is the frequency and the rest are the indecies where the word is found in the document
        HashMap<String, ArrayList<Integer>> index = new HashMap<String, ArrayList<Integer>>();
        //hash map for unstemmed words for exact search
        HashMap<String, ArrayList<Integer>> indexUnstemmed = new HashMap<String, ArrayList<Integer>>();

        WordProcessor wp = new WordProcessor();
        String[] words = documentContent.split("[,!./ ]+");
        String[] wordsTitle=title.split("[,!./ ]+");
        String[] wordsH1=h1.split("[,!./ ]+");
        String[] wordsH2=h2.split("[,!./ ]+");
        String[] wordsSpacePlitOnly=documentContent.split(" ");
        String stemmedWord;
        //loop through the words in the document content
        //if the word is not a stop word, stem it and add it to the index with count 1
        //if the word is already in the index, increment the count by 1
        for (int i = 0; i < words.length; i++) {
            String wordNoSpecial = words[i].replaceAll("[^a-zA-Z]", "");
            stemmedWord = wp.processWord(wordNoSpecial);
            if (wordNoSpecial!="")
            {
                wordNoSpecial=wordNoSpecial.toLowerCase();
                if (indexUnstemmed.containsKey(wordNoSpecial)==false) {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    indecies.add(i);
                    indexUnstemmed.put(wordNoSpecial, indecies);
                }
                else {
                    ArrayList<Integer> ListOfFrequencyIndecies = indexUnstemmed.get(wordNoSpecial);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    ListOfFrequencyIndecies.add(i);
                    indexUnstemmed.put(wordNoSpecial, ListOfFrequencyIndecies);
                }
            }
            if (stemmedWord != null && !stemmedWord.equals("") ) {
                 //remove (",',?,-,.,!,@,#,$,%,^,&,*,(,),{,},[,],/,<,>,:,;)
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
        
    
        //loop through the words in the title
        //if the word is not a stop word, stem it and add it to the index with count 1
        //if the word is already in the index, increment the count by 1
        //add another integer to the indecies arraylist to indicate that this word is in the title at the end of the indecies arraylist
        for (int i = 0; i < wordsTitle.length; i++) {
            String wordNoSpecial = words[i].replaceAll("[^a-zA-Z]", "");
            
            stemmedWord = wp.processWord(wordNoSpecial);

            if(wordNoSpecial!="")
            {
                wordNoSpecial=wordNoSpecial.toLowerCase();
                if(indexUnstemmed.containsKey(wordNoSpecial)==false)
                {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -1 indicates that this word is in the title
                    indecies.add(-1);
                    indexUnstemmed.put(wordNoSpecial, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = indexUnstemmed.get(wordNoSpecial);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-1)
                        ListOfFrequencyIndecies.add(-1);
                    indexUnstemmed.put(wordNoSpecial, ListOfFrequencyIndecies);
                }
            }

            if (stemmedWord != null && !stemmedWord.equals("")) {
                //remove (",',?,-,.,!,@,#,$,%,^,&,*,(,),{,},[,],/,<,>,:,;)
                if (index.containsKey(stemmedWord)==false) {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -1 indicates that this word is in the title
                    indecies.add(-1);
                    index.put(stemmedWord, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = index.get(stemmedWord);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-1)
                        ListOfFrequencyIndecies.add(-1);
                    index.put(stemmedWord, ListOfFrequencyIndecies);
                    
                }
            }
        }
        //loop through the words in the h1
        //if the word is not a stop word, stem it and add it to the index with count 1
        //if the word is already in the index, increment the count by 1
        //add another integer to the indecies arraylist to indicate that this word is in the h1 at the end of the indecies arraylist
        for (int i = 0; i < wordsH1.length; i++) {
            String wordNoSpecial = words[i].replaceAll("[^a-zA-Z]", "");
            stemmedWord = wp.processWord(wordNoSpecial);
            if(wordNoSpecial!="")
            {
                wordNoSpecial=wordNoSpecial.toLowerCase();
                if(indexUnstemmed.containsKey(wordNoSpecial)==false)
                {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -2 indicates that this word is in the h1
                    indecies.add(-2);
                    indexUnstemmed.put(wordNoSpecial, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = indexUnstemmed.get(wordNoSpecial);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-2)
                        ListOfFrequencyIndecies.add(-2);
                    indexUnstemmed.put(wordNoSpecial, ListOfFrequencyIndecies);
                }
            }

            if (stemmedWord != null && !stemmedWord.equals("")) {
                //remove (",',?,-,.,!,@,#,$,%,^,&,*,(,),{,},[,],/,<,>,:,;)
                
                if (index.containsKey(stemmedWord)==false) {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -2 indicates that this word is in the h1
                    indecies.add(-2);
                    index.put(stemmedWord, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = index.get(stemmedWord);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-2)
                        ListOfFrequencyIndecies.add(-2);
                    index.put(stemmedWord, ListOfFrequencyIndecies);
                    
                }
            }
        }
        //loop through the words in the h2
        //if the word is not a stop word, stem it and add it to the index with count 1
        //if the word is already in the index, increment the count by 1
        //add another integer to the indecies arraylist to indicate that this word is in the h2 at the end of the indecies arraylist
        for (int i = 0; i < wordsH2.length; i++) {
            String wordNoSpecial = words[i].replaceAll("[^a-zA-Z]", "");
            stemmedWord = wp.processWord(wordNoSpecial);
            if(wordNoSpecial!="")
            {
                wordNoSpecial=wordNoSpecial.toLowerCase();
                if(indexUnstemmed.containsKey(wordNoSpecial)==false)
                {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -3 indicates that this word is in the h2
                    indecies.add(-3);
                    indexUnstemmed.put(wordNoSpecial, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = indexUnstemmed.get(wordNoSpecial);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-3)
                        ListOfFrequencyIndecies.add(-3);
                    indexUnstemmed.put(wordNoSpecial, ListOfFrequencyIndecies);
                }
            }
            if (stemmedWord != null && !stemmedWord.equals("")) {
                //remove (",',?,-,.,!,@,#,$,%,^,&,*,(,),{,},[,],/,<,>,:,;)
                if (index.containsKey(stemmedWord)==false) {
                    ArrayList<Integer> indecies = new ArrayList<Integer>();
                    indecies.add(1);
                    //index of -3 indicates that this word is in the h2
                    indecies.add(-3);
                    index.put(stemmedWord, indecies);
                }
                else {// was in the dictionary before
                    ArrayList<Integer> ListOfFrequencyIndecies = index.get(stemmedWord);
                    ListOfFrequencyIndecies.set(0, ListOfFrequencyIndecies.get(0) + 1);
                    if(ListOfFrequencyIndecies.get(ListOfFrequencyIndecies.size()-1)!=-3)
                        ListOfFrequencyIndecies.add(-3);
                    index.put(stemmedWord, ListOfFrequencyIndecies);
                    
                }
            }
        }
        int totalWords = words.length;
        //create an object of the IndexedWord class
        //arraylist to store the indexed words
        ArrayList<IndexedWord> indexedWords = new ArrayList<IndexedWord>();
        ArrayList<IndexedWord> indexedWordsUnstemmed = new ArrayList<IndexedWord>();
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
            indexedWord.setTitle(Linktitle);
            //check on the size of the list of indecies
            while(listS.get(listS.size()-1)<0)
            {
                if(listS.get(listS.size()-1)==-1)
                    {
                        indexedWord.setInTitle(true);
                        //remove the -ve number from the list of indecies
                        listS.remove(listS.size()-1);
                    }
                else if(listS.get(listS.size()-1)==-2)
                    {
                        indexedWord.setInH1(true);
                    //remove the -ve number from the list of indecies
                    listS.remove(listS.size()-1);
                    }
                else if(listS.get(listS.size()-1)==-3)
                {

                    indexedWord.setInH2(true);
                    //remove the -ve number from the list of indecies
                    listS.remove(listS.size()-1);

                }
            }
                
            //remove the frequency from the list of indecies
            listS.remove(0);
            indexedWord.setIndecies(listS);
            //construct a sentence from the indecies of the word to add it to the content of the word
            String sentence = "";
            if (listS.size() > 0) {
                
                int FirstInstance = listS.get(0);
                //construct a word from the first instance of the word
                for(int i=-4;i<5;i++)
                {
                    if(FirstInstance+i>=0 && FirstInstance+i<words.length)
                    {
                        sentence+=words[FirstInstance+i]+" ";
                    }
                        

                }
                indexedWord.setContent(sentence);
            }
            else{
                indexedWord.setContent("No content");
                }
            indexedWords.add(indexedWord);

        
        }

        //loop throught the indexUnstemmed and calculate the parameters for each word
        for(String word : indexUnstemmed.keySet())
        {
            ArrayList<Integer> listS = indexUnstemmed.get(word);
            int frequency = listS.get(0);
            double tf = (double)frequency / (double)totalWords;
            IndexedWord indexedWord = new IndexedWord();
            indexedWord.setWord(word);
            indexedWord.setFrequency(frequency);
            indexedWord.setTf(tf);
            indexedWord.setUrl(url);
            indexedWord.setTitle(Linktitle);
            //check on the size of the list of indecies
            while(listS.get(listS.size()-1)<0)
            {
                if(listS.get(listS.size()-1)==-1)
                    {
                        indexedWord.setInTitle(true);
                        //remove the -ve number from the list of indecies
                        listS.remove(listS.size()-1);
                    }
                else if(listS.get(listS.size()-1)==-2)
                    {
                        indexedWord.setInH1(true);
                    //remove the -ve number from the list of indecies
                    listS.remove(listS.size()-1);
                    }
                else if(listS.get(listS.size()-1)==-3)
                {

                    indexedWord.setInH2(true);
                    //remove the -ve number from the list of indecies
                    listS.remove(listS.size()-1);

                }
            }
                
            //remove the frequency from the list of indecies
            listS.remove(0);
            indexedWord.setIndecies(listS);
            //construct a sentence from the indecies of the word to add it to the content of the word
            String sentence = "";
            if (listS.size() > 0) {
                
                int FirstInstance = listS.get(0);
                //construct a word from the first instance of the word
                for(int i=-4;i<5;i++)
                {
                    if(FirstInstance+i>=0 && FirstInstance+i<words.length)
                    {
                        sentence+=words[FirstInstance+i]+" ";
                    }
                        

                }
                indexedWord.setContent(sentence);
            }
            else{
                indexedWord.setContent("No content");
            }
            indexedWordsUnstemmed.add(indexedWord);
        }
        //return the indexed arrays
        ArrayList<ArrayList<IndexedWord>> indexedWordsArray = new ArrayList<ArrayList<IndexedWord>>();
        //indexedWordsArray[0] is the stemmed words
        //indexedWordsArray[1] is the unstemmed words

        indexedWordsArray.add(indexedWords);
        indexedWordsArray.add(indexedWordsUnstemmed);

        return indexedWordsArray;
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
         ArrayList<ArrayList<IndexedWord>> indexedWordss = indexer.index(documentContent,"https://www.google.com", "Information", "Limit", "Contacted","title");
         ArrayList<IndexedWord> indexedWords = indexedWordss.get(0);
         ArrayList<IndexedWord> indexedWordsUnstemmed = indexedWordss.get(1);
         //loop through the indexed words and print them
        //  for (IndexedWord indexedWord : indexedWords) {
        //      System.out.println(indexedWord.getWord() + " " + indexedWord.getFrequency() + " " + indexedWord.getTf() + " " + indexedWord.getUrl() + " " + indexedWord.getInTitle() + " " + indexedWord.getInH1() + " " + indexedWord.getInH2());
        //      //loop through the indecies of the word and print them
        //         for (int index : indexedWord.getIndices()) {
        //             System.out.print(index + " ");
        //         }
        //         System.out.println();
        //         //print the content of the word
        //         System.out.println(indexedWord.getContent());
                

        //  }
            //loop through the indexed words and print them
            for (IndexedWord indexedWord : indexedWordsUnstemmed) {
                System.out.println(indexedWord.getWord() + " " + indexedWord.getFrequency() + " " + indexedWord.getTf() + " " + indexedWord.getUrl() + " " + indexedWord.getInTitle() + " " + indexedWord.getInH1() + " " + indexedWord.getInH2());
                //loop through the indecies of the word and print them
                for (int index : indexedWord.getIndices()) {
                    System.out.print(index + " ");
                }
                System.out.println();
                //print the content of the word
                System.out.println(indexedWord.getContent());
            }
        
    }

    
}
