package task;


public class CleanupTask implements Runnable {
    public static Integer cleanupIntervalMillisecond = 50;

    @Override
    public void run() {
        ItineraryFlight.removeNotAcceptableFares();
        System.gc();
    }
}
