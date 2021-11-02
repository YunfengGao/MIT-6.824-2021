import java.util.concurrent.ThreadLocalRandom;

public class VoteCount1 {
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

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                boolean vote = requestVote();
                if (vote) {
                    unsafeCounter.addCount();
                }
                unsafeCounter.addFinish();
            }).start();
        }

        for (; ; ) {
            if (unsafeCounter.getCount() >= 5 || unsafeCounter.getFinish() == 10) {
                break;
            }
        }

        System.out.println(unsafeCounter.getCount() >= 5 ? "received 5+ votes!" : "lost");
    }

    private static boolean requestVote() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
