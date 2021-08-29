import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对象本身并不是线程安全的，通过在操作对象时加锁保证线程安全，即:先加锁，再add；先加锁，再get
 */
public class VoteCount2 {
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
        }

        System.out.println(unsafeCounter.getCount() >= 5 ? "received 5+ votes!" : "lost");
        // 如果这里不解锁会怎样? 可能:投票阶段未执行完毕，获取不到锁而导致死锁
        lock.unlock();
    }

    private static boolean requestVote() {
        return new SecureRandom().nextBoolean();
    }
}
