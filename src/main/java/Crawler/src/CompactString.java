package Crawler.src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CompactString {

    private HashSet<String> compactStrings;
    private String fileName;
    private FileWriter myWriter;

    public CompactString(String fileName, boolean loadFromFile) {
        compactStrings = new HashSet<String>();
        this.fileName = fileName;

        try {
            myWriter = new FileWriter(fileName, loadFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(loadFromFile)
        {
            this.set(new FileHandler(fileName).readFileInHashSet());
        }

    }

    public synchronized void add(String text)
    {
        compactStrings.add(text);
    }

    public synchronized boolean contains(String text) {
        if(compactStrings.contains(text))
            return true;

        compactStrings.add(text);
        return false;
    }

    public void set(HashSet<String> visitedLinks)
    {
        this.compactStrings = visitedLinks;
    }

    public HashSet<String> get()
    {
        return this.compactStrings;
    }

}
