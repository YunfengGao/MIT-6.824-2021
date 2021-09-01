import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentMutex {

    static class FetchState {
        private final Lock lock = new ReentrantLock();
        private final Set<String> fetched = new HashSet<>();
    }

    public void craw(String url, FakeFetcher fakeFetcher, FetchState fetchState) {
        fetchState.lock.lock();
        boolean already = fetchState.fetched.contains(url);
        fetchState.fetched.add(url);
        fetchState.lock.unlock();

        if (already) {
            return;
        }

        List<String> urls = fakeFetcher.Fetch(url);
        if (urls == null) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(urls.size());
        for (String u : urls) {
            new Thread(() -> {
                craw(u, fakeFetcher, fetchState);
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConcurrentMutex concurrentMutex = new ConcurrentMutex();
        FakeFetcher fetcher = new FakeFetcher();
        FetchState fetchState = new FetchState();

        concurrentMutex.craw("http://golang.org/", fetcher, fetchState);
    }
}
