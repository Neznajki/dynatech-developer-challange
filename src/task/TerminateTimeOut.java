package task;

import helper.Debug;

public class TerminateTimeOut implements Runnable {
    public static int taskTimeoutSecond = 50;

    @Override
    public void run() {
        TaskExecutor.terminateTask = true;
        if (Debug.isDebug) {
            System.out.println("terminating task due timeout");
        }
    }
}
