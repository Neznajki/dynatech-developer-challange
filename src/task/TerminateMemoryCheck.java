package task;

import helper.MemoryHelper;

public class TerminateMemoryCheck implements Runnable {
    public static Integer taskTimeoutSecond = 1;

    @Override
    public void run() {
        System.gc();
        if (MemoryHelper.getMemoryUsagePercent()  >= 90) {
            TaskExecutor.terminateTask = true;
        }
    }
}
