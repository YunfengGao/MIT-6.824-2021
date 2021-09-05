import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ConcurrentChannel {
    public void craw(String url, FakeFetcher fakeFetcher) {
        BlockingQueue<List<String>> blockingQueue = new LinkedBlockingQueue<>();

        try {
            blockingQueue.put(Collections.singletonList(url));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        coordinator(blockingQueue, fakeFetcher);
    }

    public void coordinator(BlockingQueue<List<String>> blockingQueue, FakeFetcher fakeFetcher) {
        int n = 1;
        Set<String> fetched = new HashSet<>();
        while (!blockingQueue.isEmpty()){
            try {
                List<String> urls = blockingQueue.take();
                for (String url : urls) {
                    if (!fetched.contains(url)) {
                        fetched.add(url);
                        n += 1;
                        worker(url, blockingQueue, fakeFetcher);
                    }
                }
                n -= 1;
                if (n == 0) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void worker(String url, BlockingQueue<List<String>> blockingQueue, FakeFetcher fakeFetcher) {
        List<String> urls = fakeFetcher.Fetch(url);
        if (urls != null) {
            try {
                blockingQueue.put(urls);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentChannel concurrentChannel = new ConcurrentChannel();
        FakeFetcher fetcher = new FakeFetcher();
        concurrentChannel.craw("http://golang.org/", fetcher);
    }
}
