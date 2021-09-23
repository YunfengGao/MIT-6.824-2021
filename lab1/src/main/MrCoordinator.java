package main;

import java.util.concurrent.TimeUnit;
import mr.Coordinator;

/**
 * start the coordinator process, which is implemented in ../mr/Coordinator.java
 * Please do not change this file.
 */
public class MrCoordinator {

    public static void main(String[] args) throws InterruptedException {
        Coordinator m = Coordinator.makeCoordinator(args, 10);
        while (!m.isDone()) {
            TimeUnit.SECONDS.sleep(1);
        }
        TimeUnit.SECONDS.sleep(1);
    }
}
