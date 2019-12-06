package task;

import concurrency.Worker;
import concurrency.WorkerSupervisor;
import helper.Debug;
import helper.Timer;
import json.FareDeserializerJackson;
import json.FareDeserializerTask;
import json.Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class Execute {
    public static void main(String[] args) {
        Reader reader = new Reader();

        try {
            Timer timer = new Timer();
            int nThreads = Runtime.getRuntime().availableProcessors() - 1;
            if (nThreads <= 0) {
                nThreads = 1;
            }
            WorkerSupervisor workerSupervisor = new WorkerSupervisor(nThreads);

            System.out.println(Runtime.getRuntime().availableProcessors());

            String dataFileName = System.getenv("DATA_FILE");
//            String dataFileName = "/work/java/dynatech-challange/example-data_league-5-big-path.json";
//            String dataFileName = "/work/java/dynatech-challange/generated_100_500000.json";

            System.out.println(String.format("parsing file %s", dataFileName));

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> futureFileStreamTerminator = executor.schedule(new PriceCollectionTerminator(), PriceCollectionTerminator.taskTimeoutSecond, TimeUnit.SECONDS);
            ScheduledFuture<?> futureCleanup = executor.scheduleAtFixedRate(new CleanupTask(), 0, CleanupTask.cleanupIntervalMillisecond, TimeUnit.MILLISECONDS);

//            workerSupervisor.startWorking(true);
//            reader.getDataObjectGson(dataFileName);
            reader.getDataObjectJackson(dataFileName);

//            workerSupervisor.waitUntilDone(4);
            timer.showExecutionTime("file parsing took");
            AllFareCollector allFareCollector = AllFareCollector.getInstance();
            allFareCollector.prepare();
            timer.showExecutionTime("file preparing took");
//            BestFareCollection.getInstance().removeNullsAndReverse();
            Debug.debugTrace();
            System.gc();
            Debug.debugTrace();


            System.out.println(allFareCollector.getCollection().size());
            Worker.addTask(new LowestPriceGatherTask(new LowestItineraryFlight(allFareCollector.getCollection().get(0))));
//            workerSupervisor.markFareRequired();
//            workerSupervisor.startWorking();
//            workerSupervisor.waitUntilDone(PriceCollectionTerminator.taskTimeoutSecond);

            timer.showExecutionTime("price searching took");
            futureCleanup.cancel(true);
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
