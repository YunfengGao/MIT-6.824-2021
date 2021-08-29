import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 使用线程安全的队列SynchronousQueue，模拟无缓冲Channel，实现并发生产和消费
 * 和Golang里使用Channel传递数据还是不大一样
 * 相对vote1-vote4，将vote计算逻辑挪到了单线程的消费者里
 */
public class VoteCount5 {
    private static class UnsafeCounter {
        private int count;
        private int finish;

        private void addCount() {
            count++;
        }

        private int getCount() {
            return count;
        }

        private void addFinish() {
            finish++;
        }

        private int getFinish() {
            return finish;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        BlockingQueue<Boolean> blockingQueue = new SynchronousQueue<>();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    blockingQueue.put(requestVote());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        while (unsafeCounter.getCount() < 5 && unsafeCounter.getFinish() < 10) {
            boolean vote = blockingQueue.take();
            if (vote) {
                unsafeCounter.addCount();
            }
            unsafeCounter.addFinish();
        }

        System.out.println(unsafeCounter.getCount() >= 5 ? "received 5+ votes!" : "lost");
        // main线程可能没消费掉vote线程生产的全部数据，导致vote线程阻塞，需要主动结束程序
        System.exit(0);
    }

    private static boolean requestVote() {
        return new SecureRandom().nextBoolean();
    }
}
