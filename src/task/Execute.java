package task;

import helper.Debug;
import helper.Timer;
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
            Timer timer = new Timer();
            System.out.println(Runtime.getRuntime().availableProcessors());

            String dataFileName = System.getenv("DATA_FILE");
//            String dataFileName = "/work/java/dynatech-challange/example-data_league-5-big-path.json";
//            String dataFileName = "/work/java/dynatech-challange/generated_100_500000.json";

            System.out.println(String.format("parsing file %s", dataFileName));

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> futureFileStreamTerminator = executor.schedule(new PriceCollectionTerminator(), PriceCollectionTerminator.taskTimeoutSecond, TimeUnit.SECONDS);
            ScheduledFuture<?> futureTerminator = executor.schedule(new TerminateTimeOut(), TerminateTimeOut.taskTimeoutSecond, TimeUnit.SECONDS);
            ScheduledFuture<?> futureCleanup = executor.scheduleAtFixedRate(new CleanupTask(), 0, CleanupTask.cleanupIntervalMillisecond, TimeUnit.MILLISECONDS);
            ScheduledFuture<?> futureMemoryTerminator = executor.scheduleAtFixedRate(new TerminateMemoryCheck(), 0, TerminateMemoryCheck.taskTimeoutSecond, TimeUnit.SECONDS);

//            reader.getDataObjectGson(dataFileName);
            reader.getDataObjectJackson(dataFileName);
            timer.showExecutionTime("file parsing took");
            AllFareCollector allFareCollector = AllFareCollector.getInstance();
            allFareCollector.prepare();
            timer.showExecutionTime("file preparing took");
//            BestFareCollection.getInstance().removeNullsAndReverse();
            Debug.debugTrace();
            System.gc();

            Debug.debugTrace();

            LowestPriceGatherTask task = new LowestPriceGatherTask(
                new LowestItineraryFlight(allFareCollector.getCollection().get(0))
            );

            task.run();

            while(LowestPriceGatherTask.nextFlightSearchTask != null) {
                LowestPriceGatherTask.nextFlightSearchTask.run();
            }
            timer.showExecutionTime("price searching took");
//            executePriceGatherTask();
//            futureCleanup.cancel(true);
            futureTerminator.cancel(true);
            futureCleanup.cancel(true);
            futureMemoryTerminator.cancel(true);
            futureFileStreamTerminator.cancel(true);
            executor.shutdown();
//			TaskExecutor.executeConcurrency();//TODO make this

            Debug.debugTrace();

            String jsonFileContents;
            if (ItineraryFlight.getMinPriced() != null) {
                jsonFileContents = reader.convertObjectArrayToJsonString(ItineraryFlight.getMinPriced().getFreIds());
            } else if (LowestPriceGatherTask.lowestItineraryFlight != null) {
                jsonFileContents = reader.convertObjectArrayToJsonString(LowestPriceGatherTask.lowestItineraryFlight.getFreIds());
            } else {
                throw new RuntimeException("no fare found in timeout");
            }

            System.out.println(jsonFileContents);

            writeToFile(jsonFileContents);

            System.exit(0);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace();}
            System.exit(666);
        }

    }

    private static void executePriceGatherTask() {
        for (Fare fare : BestFareCollection.getInstance().getFaresByPosition(0)) {
            try {
                TaskExecutor.addTask(new PriceGatherTask(ItineraryFlight.createInstance(fare)));
            } catch (Exception e) {
                if (Debug.isDebug) { e.printStackTrace();}
            }
        }

        TaskExecutor.executeSingleThread();
        ItineraryFlight.removeNotAcceptableFares();
    }

    private static void writeToFile(String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.getenv("RESULT_FILE")));
        writer.write(content);

        writer.close();
    }
}
