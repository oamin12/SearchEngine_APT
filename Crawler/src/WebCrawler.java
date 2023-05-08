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
	
		public static final int MAX_LINKS = 700;
		
		private String firstLink;
        private int id;
        //private ArrayList<String> visitedLinks;
        private  VisitedLinks visitedLinks;
        
        private ArrayList<Thread> runningThreads = new ArrayList<Thread>();

        private Counter counter;
        
        public Thread thread;
	
		public WebCrawler(String firstLink, int id, Counter counter, VisitedLinks visitedLinks) {
            this.firstLink = firstLink;
            this.id = id;
            this.visitedLinks = visitedLinks;
            thread = new Thread(this);
            thread.start();
            this.counter = counter;
            this.visitedLinks = visitedLinks;
        }
		
		@Override
		public void run() {
			crawl(firstLink);
            //System.out.println(counter.getCount());
		}
		
		private void crawl(String URL)
		{
			if(!counter.increment(MAX_LINKS)) {
                //System.out.println("Reached max links");
                return;
            }

            Document document = request(URL);

            if(document != null) {
                for(Element link : document.select("a[href]")) 
                {

                    String url = link.absUrl("href");
                    String normalizedURL = normalizeURL(url);
                    
                    if(!visitedLinks.isVisited(normalizedURL)) {       
                                           
//                        if(normalizedURL == null)
//                        	System.out.println("this url is null "+url);
//                        else
//                        	System.out.println("this url after norm "+normalizedURL);
                        
                        
                        System.out.println("Thread " + id + " visited " + url);
                        
                        WebCrawler crawler = new WebCrawler(normalizedURL, id, counter, visitedLinks);
                        runningThreads.add(crawler.thread);
                        //crawl(URL);
                    }
                    
                    if(!counter.increment(MAX_LINKS)) {
                        //System.out.println("Reached max links");
                        return;
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
                System.out.println("this url is a bitch " + url);
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