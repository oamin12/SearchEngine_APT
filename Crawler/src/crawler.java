import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class crawler {

	public static final int MAX_URLS = 100;
	
	public static void main(String[] args) {

        //get number of threads from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of threads: ");

        Integer MAX_THREADS = scanner.nextInt(); //number of threads
        

        FileHandler seeds = new FileHandler("seeds.txt");
        FileHandler checkpoint = new FileHandler("crawler_checkpoint.txt");
        FileHandler visitedLinksFile = new FileHandler("visited_links.txt");
       

        ArrayList<String> links = seeds.readFileInArrayList(); //get the links from the seeds file

        ArrayList<String> checkpointInfo = checkpoint.readFileInArrayList(); //get the info from the checkpoint file

        FileHandler.deleteExtraThreadsFiles(MAX_THREADS, Integer.parseInt(checkpointInfo.get(0))); //delete the files of the extra threads
        
        ArrayList<WebCrawler> crawlers = new ArrayList<WebCrawler>(); //list of all the threads
        Counter counter = new Counter(0);

        //if the last checkpoint was made with the same number of threads
        String answer;
        if(checkpointInfo.get(0).equals(MAX_THREADS.toString()))
        {
          //ask the user if he wants to continue from the checkpoint
            do {
                System.out.println("Previous crawl detected, do you want to continue from the last checkpoint? (y/n)");
                answer = scanner.next();
            } while (answer.equals("y") == false && answer.equals("Y") == false && answer.equals("n") == false && answer.equals("N") == false);
            
            if(answer.equals("y") || answer.equals("Y"))
            {
                VisitedLinks visitedLinks = new VisitedLinks("visited_links.txt", true); // list of all the visited links

                counter.set(Integer.parseInt(checkpointInfo.get(1)));
                //start all the threads and send them the links from the checkpoint
                for(int i = 0; i < MAX_THREADS; i++)
                {
                    Integer id = i+1;
                    String fileName = "thread" + id.toString() + ".txt";
                    FileHandler file = new FileHandler(fileName);
                    crawlers.add(new WebCrawler(file.readFileInQueue(), id, counter, visitedLinks, MAX_URLS, file, checkpoint, visitedLinksFile));
                }
            }         
            
        }
        else
        {
        	answer = "n";
        }
        	

		


        //if the user doesn't want to continue from the checkpoint     
        if(answer.equals("n") || answer.equals("N"))
        {
            VisitedLinks visitedLinks = new VisitedLinks("visited_links.txt", false); // list of all the visited links
        
            for(String link: links)
        	    visitedLinks.add(link); //add the links from the seeds file to the visited links list

            if(MAX_THREADS < links.size())
            {
                //give each thread only one link
                //and give the last thread the remaining links
                Integer id = 0;
                String fileName;
                for(int i = 0; i < MAX_THREADS-2; i++) {
                    Queue<String> temp = new LinkedList<>();
                    
                    temp.add(links.get(i));
                    id = i+1;
                    fileName = "thread" + id.toString() + ".txt";
                    crawlers.add(new WebCrawler(temp, id, counter, visitedLinks, MAX_URLS, new FileHandler(fileName), checkpoint, visitedLinksFile));
                }

                Queue<String> temp = new LinkedList<>();
                for(int i = MAX_THREADS-2; i < links.size(); i++) {
                    temp.add(links.get(i));
                }
                id++;
                fileName = "thread" + id.toString() + ".txt";
                crawlers.add(new WebCrawler(temp, MAX_THREADS-1, counter, visitedLinks, MAX_URLS, new FileHandler(fileName), checkpoint, visitedLinksFile));
            }
            else
            {
                for(int i = 0; i < links.size(); i++) {
                    Queue<String> temp = new LinkedList<>();
                    
                    temp.add(links.get(i));
                    Integer id = i+1;
                    String fileName = "thread" + id.toString() + ".txt";
                    crawlers.add(new WebCrawler(temp, id, counter, visitedLinks, MAX_URLS, new FileHandler(fileName), checkpoint, visitedLinksFile));
                }
            }
        }

        for(WebCrawler crawler : crawlers) {
            try {
                crawler.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //write the number of threads and the number of visited links to the checkpoint file
        checkpoint.writeCheckpoint(MAX_THREADS.toString(), String.valueOf(counter.getCount()));

        scanner.close();
        
        System.out.println(counter.getCount());	
	}

}
