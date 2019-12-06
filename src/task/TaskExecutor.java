package task;

import helper.Debug;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
    public static ArrayList<PriceGatherTask> priceGatherTasks = new ArrayList<>();
    public static ExecutorService threadPool = null;
    public static boolean singleThreadExecution = false;
    public static boolean terminateTask = false;

    public static synchronized void addTask(PriceGatherTask priceGatherTask)
    {
        if (threadPool != null) {
            threadPool.execute(priceGatherTask);
            return;
        }

        if (singleThreadExecution) {
            if (terminateTask && ItineraryFlight.getMinPriced() != null) {
                return;
            }
            priceGatherTask.run();
            return;
        }

        priceGatherTasks.add(priceGatherTask);
    }

    public static void executeSingleThread()
    {
        singleThreadExecution = true;
        for (PriceGatherTask task: priceGatherTasks) {
            task.run();
            if (terminateTask) {
                break;
            }
        }
    }

    public static void executeConcurrency()
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        for (PriceGatherTask task: priceGatherTasks) {
				threadPool.execute(task);
        }

        try {
            if (!threadPool.awaitTermination(40, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            if (Debug.isDebug) { e.printStackTrace(); }

            threadPool.shutdown();
            Thread.currentThread().interrupt();
        }
    }

    public static void shutdownConcurrent()
    {
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }
}
