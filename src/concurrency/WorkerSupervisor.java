package concurrency;

import helper.Debug;
import task.ItineraryFlight;
import task.LowestPriceGatherTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WorkerSupervisor {
    private final List<Worker> workerList = new ArrayList<>();
    private ExecutorService threadPool;
    private Integer nThreads;
    private boolean waitForTask;
    private boolean canTerminateWithoutFare = true;
    private boolean terminated = false;
    private boolean strictTaskWaitingFinish = false;
    private LocalDateTime began;

    public WorkerSupervisor(Integer nThreads) {
        this.nThreads = nThreads;
        this.began = LocalDateTime.now();
    }

    public void markFareRequired()
    {
        this.canTerminateWithoutFare = false;
    }

    public void createWorkers(int amount)
    {
        for(int i=0; i<amount; i++){
            this.workerList.add(new Worker(this));
        }
    }

    public void startWorking(boolean strictTaskWaitingFinish)
    {
        this.strictTaskWaitingFinish = strictTaskWaitingFinish;
        this.startWorking();
    }

    public void startWorking()
    {
        this.threadPool = Executors.newFixedThreadPool(nThreads);
        this.createWorkers(this.nThreads);
        this.waitForTask = true;

        try {
            for (Worker worker: this.workerList) {
                this.threadPool.execute(worker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitUntilDone(int timeout)
    {
        timeout -= Duration.between(this.began, LocalDateTime.now()).toSeconds();
        if (this.strictTaskWaitingFinish) {
            this.waitForTask = false;
            this.strictTaskWaitingFinish = false;
        }
        try {
            if (!this.threadPool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                this.threadPool.shutdownNow();
            }
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace(); }

            this.threadPool.shutdown();
            Thread.currentThread().interrupt();
        }

        this.workerList.clear();
    }

    public void onWorkerException()
    {
        this.terminated = true;
        this.threadPool.shutdown();
    }

    public boolean shouldStop()
    {
        if (! this.waitForTask) {
            this.checkActivity();
        }

        if (this.terminated) {
            if (this.canTerminateWithoutFare) {
                this.threadPool.shutdown();
                return true;
            } else if (LowestPriceGatherTask.lowestItineraryFlight != null || ItineraryFlight.getMinPriced() != null) {
                this.threadPool.shutdown();
                return true;
            }
        }

        return false;
    }

    public void repeatTask(Worker worker)
    {
        try {
            this.threadPool.execute(worker);
        } catch (Exception e) {
            if (Debug.isDebug) {
                e.printStackTrace();
            }
        }
    }

    private void checkActivity()
    {
        int busyTasks = 0;
        for (Worker worker: this.workerList) {
            if (! worker.wasPaused()) {
                busyTasks++;
            }
        }

        if (busyTasks == 0) {
            this.terminated = true;
        }
    }
}
