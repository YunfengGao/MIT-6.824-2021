import java.security.SecureRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * VoteCount4使用Condition，通过让main线程等待，并主动唤醒检查条件的方式，减少cpu的忙等待
 * 相比VoteCount3的sleep，vote线程等待的时间更精确
 */
public class VoteCount4 {
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

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                boolean vote = requestVote();
                lock.lock();
                if (vote) {
                    unsafeCounter.addCount();
                }
                unsafeCounter.addFinish();
                condition.signal();
                lock.unlock();
            }).start();
        }

        lock.lock();
        while (unsafeCounter.getCount() < 5 && unsafeCounter.getFinish() != 10) {
            condition.await();
        }
        lock.unlock();

        System.out.println(unsafeCounter.getCount() >= 5 ? "received 5+ votes!" : "lost");
    }

    private static boolean requestVote() {
        return new SecureRandom().nextBoolean();
    }
}
