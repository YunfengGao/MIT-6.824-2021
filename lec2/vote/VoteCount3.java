import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 在VoteCount2的基础上，查询时增加了sleep50MillSeconds方法，减少cpu的忙等待
 */
public class VoteCount3 {
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

    public static void main(String[] args) {
        UnsafeCounter unsafeCounter = new UnsafeCounter();

        Lock lock = new ReentrantLock();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                boolean vote = requestVote();
                lock.lock();
                if (vote) {
                    unsafeCounter.addCount();
                }
                unsafeCounter.addFinish();
                lock.unlock();
            }).start();
        }

        // 如果这里的读操作不想加锁或者synchronized怎么处理? volatile unsafeCounter
        for (; ; ) {
            lock.lock();
            if (unsafeCounter.getCount() >= 5 || unsafeCounter.getFinish() == 10) {
                break;
            }
            lock.unlock();
            // 如果把sleep放在unlock之前会怎样? 这里的lock为默认的非公平锁，main线程与vote线程竞争，可能导致vote线程饥饿
            sleep50MillSeconds();
        }

        System.out.println(unsafeCounter.getCount() >= 5 ? "received 5+ votes!" : "lost");
        // 如果这里不解锁会怎样? 可能:投票阶段未执行完毕，获取不到锁而导致死锁
        lock.unlock();
    }

    private static void sleep50MillSeconds() {
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean requestVote() {
        return new SecureRandom().nextBoolean();
    }
}
