import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Serial {
    private final Set<String> pool;

    public Serial() {
        pool = new HashSet<>();
    }

    public void craw(String url, FakeFetcher fakeFetcher) {
        if (pool.contains(url)) {
            return;
        }
        pool.add(url);
        List<String> urls = fakeFetcher.Fetch(url);
        if (urls == null) {
            return;
        }
        for (String u : urls) {
            craw(u, fakeFetcher);
        }
    }

    public static void main(String[] args) {
        Serial serial = new Serial();
        FakeFetcher fetcher = new FakeFetcher();

        serial.craw("http://golang.org/", fetcher);
    }
}