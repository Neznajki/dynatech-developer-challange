package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Worker implements Runnable {
    public static List<Runnable> tasksList = new ArrayList<>();
    public static boolean taskDone = false;

    public static synchronized void addTask(Runnable task)
    {
//        task.run();
        tasksList.add(task);
    }

    private static synchronized Runnable getTask()
    {
        if (tasksList.size() > 0) {
            Runnable result = tasksList.get(0);
            tasksList.remove(0);

            return result;
        }

        return null;
    }

    private boolean wasPaused = false;
    private WorkerSupervisor workerSupervisor;

    public Worker(WorkerSupervisor workerSupervisor)
    {
        this.workerSupervisor = workerSupervisor;
    }

    @Override
    public void run() {
        if (this.workerSupervisor.shouldStop()) {
            return;
        }
        Runnable task = getTask();

        if (task == null) {
            this.wasPaused = true;
            try {
                TimeUnit.MILLISECONDS.sleep(500);

                task = getTask();
                if (task == null) {
                    this.workerSupervisor.repeatTask(this);

                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.workerSupervisor.onWorkerException();

                return;
            }
        }

        this.wasPaused = false;
        task.run();

        this.workerSupervisor.repeatTask(this);
    }

    public boolean wasPaused() {
        return this.wasPaused;
    }
}
