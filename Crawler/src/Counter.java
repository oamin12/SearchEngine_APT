public class Counter {
        private int count = 0;

        public Counter(int val) {
            count = val;
        }

        public synchronized boolean increment(int number) {
            if(count < number) {
                count++;
                return true;
            }
            return false;
        }

        public synchronized int getCount() {
            return count;
        }
        
        public synchronized boolean isReached(int val)
        {
        	return count >= val;
        }

    }
