package task;


import helper.Debug;

public class CleanupTask implements Runnable {
    public static Integer cleanupIntervalMillisecond = 5000;

    @Override
    public void run() {
        ItineraryFlight.removeNotAcceptableFares();
        System.gc();
        Debug.debugTrace();
    }
}
