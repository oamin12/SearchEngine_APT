package Crawler.src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class VisitedLinks {
        private HashSet<String> visitedLinks;
        private String fileName;
        private FileWriter myWriter;

        public VisitedLinks(String fileName, boolean loadFromFile) {
            visitedLinks = new HashSet<String>();
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
        
        public synchronized void add(String url)
        {
        	visitedLinks.add(url);
        }

        public synchronized boolean isVisited(String url) {
            if(visitedLinks.contains(url)) 
                return true;
            
            visitedLinks.add(url);
            return false;
        }

        public void set(HashSet<String> visitedLinks)
        {
        	this.visitedLinks = visitedLinks;
        }

        public HashSet<String> get()
        {
        	return this.visitedLinks;
        }
}