import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 使用线程安全的队列SynchronousQueue，通过控制消费数量和生产数量相等，结束程序
 */
public class VoteCount6 {
    public static void main(String[] args) throws InterruptedException {
        int count = 0;
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

        // 这里能保证每个vote线程生产的结果都被消费，不再需要统计已消费的数量finish，也不需要主动结束程序了
        for (int i = 0; i < 10; i++) {
            boolean vote = blockingQueue.take();
            if (vote) {
                count++;
            }
        }
        System.out.println(count >= 5 ? "received 5+ votes!" : "lost");
    }

    private static boolean requestVote() {
        return new SecureRandom().nextBoolean();
    }
}
