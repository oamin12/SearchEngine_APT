import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class WebCrawler implements Runnable {
	
		private int MAX_LINKS;
		private Queue<String> firstLinks;
        private int id;
        private  VisitedLinks visitedLinks;

        private Counter counterURL;
        
        public Thread thread;
	
		public WebCrawler(Queue<String> firstLinks, int id, Counter counterURL, VisitedLinks visitedLinks, int maxURLS) {
			
            this.firstLinks = firstLinks;
            
            this.id = id;
            this.visitedLinks = visitedLinks;
            thread = new Thread(this);
            thread.start();
            this.counterURL = counterURL;
            this.visitedLinks = visitedLinks;
            this.MAX_LINKS = maxURLS;
         
        }
		
		@Override
		public void run() {
			crawl(firstLinks);
            //System.out.println(counter.getCount());
		}
		
		private void crawl(Queue<String> qURL)
		{
            
            while(!qURL.isEmpty())
            {
            	if(!counterURL.increment(MAX_LINKS)) 
                    return;
            	
                String URL = qURL.poll();
                Document document = request(URL);
                
                System.out.println("Thread " + this.id + " visited " + URL); 

                if(document != null) {
                    for(Element link : document.select("a[href]")) 
                    {

                        String url = link.absUrl("href");
                        String normalizedURL = normalizeURL(url);
                         
                        if(!visitedLinks.isVisited(normalizedURL)) 
                        {                      
                            qURL.add(url);               
                        }          
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
                System.out.println("my id is "+this.id+" this url is a bitch " + url);
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
        
        private boolean checkRobotsTXT(String url) throws IOException
        {
        	URL targetUrl = new URL(url);
            String domain = targetUrl.getHost();

            URL robotsUrl = new URL("https://" + domain + "/robots.txt");
            System.out.println(robotsUrl.toString());
            URLConnection connection = robotsUrl.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            boolean isDisallowed = false;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("User-agent: (.*)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String userAgent = matcher.group(1);
                    if (userAgent.equals("*")) {
                        pattern = Pattern.compile("Disallow: (.*)");
                        matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String disallowDirective = matcher.group(1);
                            if (targetUrl.getPath().startsWith(disallowDirective)) {
                                isDisallowed = true;
                                break;
                            }
                        }
                    }
                }
            }

            reader.close();
            inputStream.close();

            if (isDisallowed) {
                System.out.println("Target URL is disallowed by robots.txt");
                return false;
                // Don't crawl the page
            } else {
                System.out.println("Target URL is allowed by robots.txt");
                return true;
                // Crawl the page
            }
        }
		
	}