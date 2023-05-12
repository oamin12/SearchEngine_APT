import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class crawler {

	public static final int MAX_THREADS = 20;
	public static final int MAX_URLS = 10;
	
	public static void main(String[] args) {
		ArrayList<String> links = readFromFile("seeds.txt");
        ArrayList<WebCrawler> crawlers = new ArrayList<WebCrawler>();

        Counter counter = new Counter(0);
        VisitedLinks visitedLinks = new VisitedLinks();
        
        
        for(String link: links)
        	visitedLinks.add(link);
        
        
        if(MAX_THREADS < links.size())
        {
            //give each thread only one link
        	//and give the last thread the remaining links
            for(int i = 0; i < MAX_THREADS-2; i++) {
            	Queue<String> temp = new LinkedList<>();
            	
            	temp.add(links.get(i));
            	
                crawlers.add(new WebCrawler(temp, i, counter, visitedLinks, MAX_URLS));
            }

            Queue<String> temp = new LinkedList<>();
            for(int i = MAX_THREADS-2; i < links.size(); i++) {
            	temp.add(links.get(i));
            }
            crawlers.add(new WebCrawler(temp, MAX_THREADS-1, counter, visitedLinks, MAX_URLS));
        }
        else
        {
        	for(int i = 0; i < links.size(); i++) {
            	Queue<String> temp = new LinkedList<>();
            	
            	temp.add(links.get(i));
            	
                crawlers.add(new WebCrawler(temp, i+1, counter, visitedLinks, MAX_URLS));
            }
        }

        for(WebCrawler crawler : crawlers) {
            try {
                crawler.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println(counter.getCount());	
	}
	
	public static ArrayList<String> readFromFile(String name){	

        try {
            Scanner scanner = new Scanner(new File(name));
            String line = null;
            ArrayList<String> seeds = new ArrayList<String>();
            while (scanner.hasNextLine())
            {
                line = scanner.nextLine();
                seeds.add(line);
            }
            scanner.close();
            return seeds;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

}
