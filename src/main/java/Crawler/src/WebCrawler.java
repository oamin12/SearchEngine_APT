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
                db.insert(myURL, document.title(), document.body().text());
                System.out.println("Thread " + this.id + " visited " + myURL);

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


//        private boolean checkRobotsTXT(String url) throws IOException
//        {
//        	URL targetUrl = new URL(url);
//            String domain = targetUrl.getHost();
//
//            URL robotsUrl = new URL("https://" + domain + "/robots.txt");
//            System.out.println(robotsUrl.toString());
//            URLConnection connection = robotsUrl.openConnection();
//            InputStream inputStream = connection.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line = "";
//            boolean isDisallowed = false;
//            while ((line = reader.readLine()) != null) {
//                Pattern pattern = Pattern.compile("User-agent: (.*)");
//                Matcher matcher = pattern.matcher(line);
//                if (matcher.find()) {
//                    String userAgent = matcher.group(1);
//                    if (userAgent.equals("*")) {
//                        pattern = Pattern.compile("Disallow: (.*)");
//                        matcher = pattern.matcher(line);
//                        if (matcher.find()) {
//                            String disallowDirective = matcher.group(1);
//                            if (targetUrl.getPath().startsWith(disallowDirective)) {
//                                isDisallowed = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//            reader.close();
//            inputStream.close();
//
//            if (isDisallowed) {
//                System.out.println("Target URL is disallowed by robots.txt");
//                return false;
//                // Don't crawl the page
//            } else {
//                System.out.println("Target URL is allowed by robots.txt");
//                return true;
//                // Crawl the page
//            }
//        }

	}