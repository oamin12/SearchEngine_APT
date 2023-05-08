import java.util.HashSet;

public class VisitedLinks {
        private HashSet<String> visitedLinks;

        public VisitedLinks() {
            visitedLinks = new HashSet<String>();
        }

        public synchronized boolean isVisited(String url) {
            if(visitedLinks.contains(url)) 
                return true;
            
            visitedLinks.add(url);
            return false;
        }
}