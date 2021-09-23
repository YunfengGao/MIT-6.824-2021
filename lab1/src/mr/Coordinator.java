package mr;

public class Coordinator {
    public boolean isDone() {
        return true;
    }

    //
    // start a thread that listens for RPCs from worker.java
    //
    private void server() {

    }

    //
    // create a Coordinator.
    // main/MrCoordinator.java calls this function.
    // nReduce is the number of reduce tasks to use.
    //
    public static Coordinator makeCoordinator(String[] files, int nReduce) {
        Coordinator coordinator = new Coordinator();

        // Your code here.

        coordinator.server();
        return coordinator;
    }
}
