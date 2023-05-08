import java.io.IOException;
import java.util.ArrayList;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class WebCrawler implements Runnable {
	
		public static final int MAX_LINKS = 400;
		public static final int MAX_THREADS = 50;
		
		private String firstLink;
        private int id;
        //private ArrayList<String> visitedLinks;
        private  VisitedLinks visitedLinks;
        
        private ArrayList<Thread> runningThreads = new ArrayList<Thread>();

        private Counter counterURL;
        
        public Thread thread;
	
		public WebCrawler(String firstLink, int id, Counter counterURL, VisitedLinks visitedLinks) {
            this.firstLink = firstLink;
            this.id = id;
            this.visitedLinks = visitedLinks;
            thread = new Thread(this);
            thread.start();
            this.counterURL = counterURL;
            this.visitedLinks = visitedLinks;
            
            int temp = thread.activeCount()-1;
            
            //System.out.println("I am thread"+id +" and I have brozars "+ temp);
        }
		
		@Override
		public void run() {
			crawl(firstLink);
            //System.out.println(counter.getCount());
		}
		
		private void crawl(String URL)
		{
			if(counterURL.isReached(MAX_LINKS)) 
                return;
            

            Document document = request(URL);

            if(document != null) {
                for(Element link : document.select("a[href]")) 
                {
                	
                	if(!counterURL.increment(MAX_LINKS)) 
                        return;
                    

                    String url = link.absUrl("href");
                    String normalizedURL = normalizeURL(url);
                    
                    if(!visitedLinks.isVisited(normalizedURL)) {       
                   
                        System.out.println("Thread " + this.id + " visited " + url);
                        WebCrawler crawler = new WebCrawler(url, counterURL.getCount(), counterURL, visitedLinks);
                        runningThreads.add(crawler.thread);
                        //crawl(url);
                    }
                    
                    
                }

                 for(Thread thread : runningThreads) {
                     try {
                         thread.join();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
            }
		}
		
        private Document request(String url) {
            try {
                Connection myConnection =  Jsoup.connect(url);
                Document myDocument = myConnection.get();

                if (myConnection.response().statusCode() == 200) {
                    //System.out.println("Received web page at " + url);
                    return myDocument;
                }

                return null;
            } catch (IOException e) {
                System.out.println("my id is "+this.id+"this url is a bitch " + url);
                return null;
            }
        }

        private String normalizeURL(String url){
//            try {
//                URL myURL = new URL(url);
//                return myURL.getProtocol() + "://" + myURL.getHost() + myURL.getPath();
//            } catch (Exception e) {
//                return null;
//            }
        	try {     	
        		URL myurl = new URL(url);
        		String normalizedUrlString = myurl.toURI().normalize().toString();
        		return normalizedUrlString;
        	}
        	catch (Exception e) {
              return null;
        	}
        }
		
	}