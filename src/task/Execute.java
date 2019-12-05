package task;

import helper.Debug;
import json.Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Execute {
    public static void main(String[] args) {
        Reader reader = new Reader();

        try {
            System.out.println(Runtime.getRuntime().availableProcessors());
            String dataFileName = System.getenv("DATA_FILE");
//            String dataFileName = "/work/java/dynatech-challange/src/example-data_league-5-big-path.json";
            System.out.println(String.format("parsing file %s", dataFileName));

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> futureTerminator = executor.schedule(new TerminateTimeOut(), TerminateTimeOut.taskTimeoutSecond, TimeUnit.SECONDS);
            ScheduledFuture<?> futureMemoryTerminator = executor.schedule(new TerminateMemoryCheck(), TerminateMemoryCheck.taskTimeoutSecond, TimeUnit.SECONDS);

            reader.getDataObject(dataFileName);
            BestFareCollection.getInstance().removeNullsAndReverse();
            Debug.debugTrace();
            System.gc();

            Debug.debugTrace();

            for (Fare fare : BestFareCollection.getInstance().getFaresByPosition(0)) {
                try {
                    TaskExecutor.addTask(new PriceGatherTask(ItineraryFlight.createInstance(fare)));
                } catch (Exception e) {
                    if (Debug.isDebug) { e.printStackTrace();}
                }
            }

//            ScheduledFuture<?> futureCleanup =
//                executor.scheduleAtFixedRate(new CleanupTask(), 0, CleanupTask.cleanupIntervalMillisecond, TimeUnit.MILLISECONDS);

            TaskExecutor.executeSingleThread();
            ItineraryFlight.removeNotAcceptableFares();
//            futureCleanup.cancel(true);
            futureTerminator.cancel(true);
            futureMemoryTerminator.cancel(true);
            executor.shutdown();
//			TaskExecutor.executeConcurrency();//TODO make this

            Debug.debugTrace();
            ItineraryFlight cheapestFlight = ItineraryFlight.getMinPriced();

            String jsonFileContents = reader.convertObjectArrayToJsonString(cheapestFlight.getFreIds());
            System.out.println(jsonFileContents);

            writeToFile(jsonFileContents);

            System.exit(0);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace();}
            System.exit(666);
        }

    }

    private static void writeToFile(String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.getenv("RESULT_FILE")));
        writer.write(content);

        writer.close();
    }
}
