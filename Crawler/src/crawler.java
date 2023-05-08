import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;


public class crawler {

    
	public static void main(String[] args) {
		ArrayList<String> links = readFromFile("seeds.txt");
        ArrayList<WebCrawler> crawlers = new ArrayList<WebCrawler>();

        Counter counter = new Counter(0);
        VisitedLinks visitedLinks = new VisitedLinks();

        for(int i = 0; i < links.size(); i++) {
            crawlers.add(new WebCrawler(links.get(i), i, counter, visitedLinks));
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
