package Crawler.src;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler implements Runnable {

    private final DB db;
    private int MAX_LINKS;
    private Queue<String> firstLinks;
    private int id;
    private VisitedLinks visitedLinks;
    private Map<String, Vector<String>> robotsFiles = new HashMap<String, Vector<String>>();

    private Counter counterURL;

    public Thread thread;

    private FileHandler myFile;
    private FileHandler checkpointFile;
    private FileHandler visitedLinksFile;



		public WebCrawler(Queue<String> firstLinks, int id, Counter counterURL, VisitedLinks visitedLinks, int maxURLS, FileHandler fileHandler,
            FileHandler checkpointFile, FileHandler visitedLinksFile, DB db)
        {
            this.firstLinks = firstLinks;

            this.id = id;
            this.visitedLinks = visitedLinks;

            thread = new Thread(this);
            thread.start();

            this.counterURL = counterURL;
            this.MAX_LINKS = maxURLS;

            this.myFile = fileHandler;
            this.checkpointFile = checkpointFile;
            this.visitedLinksFile = visitedLinksFile;
            this.db = db;

        }

		@Override
		public void run() {
			try {
                crawl(firstLinks);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(counter.getCount());
		}

		private void crawl(Queue<String> qURL) throws IOException
		{
            while(!qURL.isEmpty())
            {
                Queue<String> qtemp = new LinkedList<>(qURL);
                myFile.writeToFile(qtemp); //saving the links in the file

                checkpointFile.writeCheckpoint(String.valueOf(thread.activeCount()-1), String.valueOf(counterURL.getCount())); //saving the checkpoint

            	if(!counterURL.increment(MAX_LINKS)) //if we have reached the max number of URLs, stop crawling
                    return;

                String myURL = qURL.poll();//get the first URL in the queue


                if(isDisallowed(myURL))  //if robots.txt disallows this URL, skip it
                {
                    System.out.println("this URL: "+myURL+" is disallowed by robots.txt");
                    continue;
                }

                Document document = request(myURL); //request the document at this URL

                //write the visited link
                visitedLinksFile.writeToFileInLine(myURL);

                if(document != null) {
                    System.out.println("Thread " + this.id + " visited " + myURL);
                    //Get all h1 tags
                    //Put all the h1 tags in a string
                    String h1TagsString = "";
                    for(Element h1Tag : document.select("h1"))
                    {
                        h1TagsString += h1Tag.text() + " ";
                    }

                    //Get all h2 tags
                    //Put all the h2 tags in a string
                    String h2TagsString = "";
                    for(Element h2Tag : document.select("h2"))
                    {
                        h2TagsString += h2Tag.text() + " ";
                    }
                    // inserting data in the database
                    db.insert(myURL, document.title(), document.body().text(), h1TagsString, h2TagsString);


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



    public boolean isDisallowed(String url) {
        try {
            readRobotsFile(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String host = "";

        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (robotsFiles.containsKey(host)) {
            for (int i = 0; i < robotsFiles.get(host).size(); i++) {
                if (url.startsWith(robotsFiles.get(host).get(i)))
                    return true;
            }
        }

        return false;
    }



    private void readRobotsFile(String url) throws IOException {

        Vector<String> routes = new Vector<>();
        Vector<String> robotsDisallowed = new Vector<>();
        String host;

        URL temp = new URL(url);
        host = temp.getHost().toLowerCase();

        if(robotsFiles.containsKey(host))
            return;



        Document document;
        String robotsLink = temp.getProtocol()+"://"+host+"/robots.txt";
        document = request(robotsLink);

        if(document == null)
            return;

        String[] words = document.text().split(" ");

        boolean start = false;
        for (int i = 0; i < words.length-2; i++) {
            if (words[i].equals("User-agent:") && (words[i + 1].equals("*"))) {
                start = true;   //if it allows all user agents
                i += 1;
                continue;
            }
            if (start) {
                if (words[i].equals("User-agent:") && !(words[i + 1].equals("*")) || words[i].equals("#"))
                    break;

                routes.add(words[i]);
            }
        }

        for (int i = 0; i < routes.size() - 1; i++) {
            if (routes.get(i).equals("Disallow:"))
                robotsDisallowed.add(temp.getProtocol()+"://" + host + routes.get(i + 1));     //add the disallow of the domain to the disallowed list
        }

        robotsFiles.put(host, robotsDisallowed);
    }
    
}