import java.util.HashSet;

public class VisitedLinks {
        private HashSet<String> visitedLinks;

        public VisitedLinks() {
            visitedLinks = new HashSet<String>();
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
}