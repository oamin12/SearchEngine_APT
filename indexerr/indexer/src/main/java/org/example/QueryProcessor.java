package org.example;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QueryProcessor {
    QueryProcessor() {
        // this is the constructor
    }

    Set<String> ProcessQuery(ArrayList<IndexedWord> Ind, String[] Query) {
        Set<String> ProcessedQuery = new HashSet<String>();
        WordProcessor wp = new WordProcessor();
        // stem the query and add it to a string array called stemmedQuery
        ArrayList<String> stemmedQuery = new ArrayList<String>();
        for (int i = 0; i < Query.length; i++) {
            stemmedQuery.add(wp.processWord(Query[i]));
        }
        for (int i = 0; i < stemmedQuery.size(); i++) {
            if (!Query[0].equals('\"')) {
                for (int j = 0; j < Ind.size(); j++) {
                    if (Ind.get(j).getWord().equals(stemmedQuery.get(i))) {
                        ProcessedQuery.add(Ind.get(j).getUrl());
                    }
                }
            } // else// ===>
              // Phrase Searching needs new module , Since it works on the original words not
              // the stemmed ones
              // {
              // }
        }
        return ProcessedQuery;
    }

    public static void main(String[] Args) {
        // test the class
        indexer index = new indexer();
        // read the document content from the file
        String documentContent = indexer.readFileAsString(
                "D:\\Uni\\Senior 1\\Semester 2\\APT\\Project\\SearchEngine_APT\\indexerr\\indexer\\src\\main\\java\\org\\example\\content.txt");
        // index the document content
        ArrayList<IndexedWord> indexedWords = index.index(documentContent, "https://www.google.com");
        String Query = "hello world";
        String[] QueryArray = Query.split(" ");
        QueryProcessor qp = new QueryProcessor();
        qp.ProcessQuery(indexedWords, QueryArray);
    }
};
