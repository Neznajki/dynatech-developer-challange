package task;

import concurrency.Worker;
import concurrency.WorkerSupervisor;
import helper.Debug;
import helper.Timer;
import json.FareDeserializerJackson;
import json.Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public class Execute {
    public static void main(String[] args) {
        Reader reader = new Reader();

        try {
            Timer timer = new Timer();
            FareDeserializerJackson.began = LocalDateTime.now();
            int nThreads = Runtime.getRuntime().availableProcessors() - 1;
            if (nThreads <= 0) {
                nThreads = 1;
            }
            WorkerSupervisor workerSupervisor = new WorkerSupervisor(nThreads);
            FareDeserializerJackson.counter = 0;
            BestFareCollection.priceSearchLength = 2;

            System.out.println(nThreads);

//            String dataFileName = System.getenv("DATA_FILE");
            String dataFileName = "/work/java/dynatech-challange/example-data_league-5-big-path.json";
//            String dataFileName = "/work/java/dynatech-challange/generated_100_500000.json";
//            File file = new File(dataFileName);

//            if (file.length() <= 500 * 1024 * 1024) {
//                BestFareCollection.priceSearchLength = 0;
//            } else if (file.length() <= 1000 * 1024 * 1024) {
//                BestFareCollection.priceSearchLength = 4;
//            }

            System.out.println(String.format("parsing file %s", dataFileName));

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> futureFileStreamTerminator = executor.schedule(new PriceCollectionTerminator(), PriceCollectionTerminator.taskTimeoutSecond, TimeUnit.SECONDS);
            ScheduledFuture<?> futureCleanup = executor.scheduleAtFixedRate(new CleanupTask(), 0, CleanupTask.cleanupIntervalMillisecond, TimeUnit.MILLISECONDS);

//            workerSupervisor.startWorking();
            reader.collectObjectData(dataFileName);

//            workerSupervisor.waitUntilDone(4);
            timer.showExecutionTime("file parsing took");
            BestFareCollection collection = BestFareCollection.getInstance();
            collection.removeNullsAndReverse();
            timer.showExecutionTime("file preparing took");

//            BestFareCollection.getInstance().removeNullsAndReverse();
            Debug.debugTrace();
            System.gc();
            Debug.debugTrace();

            LowestItineraryFlight lowestFare = getLowestItineraryFlight(collection, 0);
            LowestItineraryFlight secondLowestFare = getLowestItineraryFlight(collection, 1);

            if (lowestFare == null || (secondLowestFare != null && secondLowestFare.getTotalPrice() < lowestFare.getTotalPrice())) {
                lowestFare = secondLowestFare;
            }

            timer.showExecutionTime("price searching took");
            futureCleanup.cancel(true);
            futureFileStreamTerminator.cancel(true);
            executor.shutdown();

            Debug.debugTrace();
            saveResultsToFile(reader, lowestFare);

            System.out.println("just try another way with filter more prices");
            FareDeserializerJackson.counter = 0;
            FareDeserializerJackson.repeatRun = true;
            BestFareCollection.priceSearchLength = 0;

            reader.collectObjectData(dataFileName);

            AllFareCollector allFareCollector = AllFareCollector.getInstance();
            allFareCollector.setCollection(BestFareCollection.getInstance().getFaresCollection());
            allFareCollector.prepare();

            System.out.println(String.format("found %d fares", allFareCollector.getCollection().size()));

            Worker.addTask(new LowestPriceGatherTask(new LowestItineraryFlight(allFareCollector.getCollection().get(0))));
            workerSupervisor.markFareRequired();
            workerSupervisor.startWorking();
            workerSupervisor.waitUntilDone(59);

            if (LowestPriceGatherTask.lowestItineraryFlight != null && LowestPriceGatherTask.lowestItineraryFlight.getTotalPrice() < lowestFare.getTotalPrice()) {
                saveResultsToFile(reader, LowestPriceGatherTask.lowestItineraryFlight);
            }

            System.exit(0);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace();}
            System.exit(666);
        }

    }

    private static void saveResultsToFile(Reader reader, LowestItineraryFlight lowestFare) throws IOException {
        String jsonFileContents = reader.convertObjectArrayToJsonString(lowestFare.getFreIds());

        System.out.println(jsonFileContents);

        writeToFile(jsonFileContents);
    }

    private static LowestItineraryFlight getLowestItineraryFlight(BestFareCollection collection, int position) {
        List<Fare> faresByPosition = collection.getFaresByPosition(position);
        LowestItineraryFlight lowestFare = new LowestItineraryFlight(faresByPosition.get(0));

        List<List<Fare>> fullFareList = collection.getFaresList();

        if (lowestFare.size() >1) {
            if (position == 0) {
                Fare fare = fullFareList.get(fullFareList.size() - 1).get(0);
                lowestFare.addFare(fare);
            } else {
                List<Fare> firstPrice = fullFareList.get(0);
                Fare fare = firstPrice.get(0);
                if (fare.getFlightSize() > 1 && firstPrice.size() == 2) {
                    fare = firstPrice.get(1);
                }

                if (fare != null) {
                    lowestFare.addFare(fare);
                }
            }
        } else if (position == 0) {
            for (int i = 1; i< fullFareList.size(); i++) {
                List<Fare> newFareByPos = fullFareList.get(i);
                if (newFareByPos.size() > 1) {
                    lowestFare.addFare(newFareByPos.get(1));
                } else {
                    lowestFare.addFare(newFareByPos.get(0));
                }
            }
        }

        if (! lowestFare.isFareFinal()) {
            lowestFare = null;
        }

        LowestItineraryFlight lowestFareSingle;
        if (position == 0 && faresByPosition.size() == 2) {
            lowestFareSingle = new LowestItineraryFlight(faresByPosition.get(1));

            for (int i = 1; i< fullFareList.size(); i++) {
                List<Fare> newFareByPos = fullFareList.get(i);
                if (newFareByPos.size() > 1) {
                    lowestFareSingle.addFare(newFareByPos.get(1));
                } else {
                    lowestFareSingle.addFare(newFareByPos.get(0));
                }
            }

            if (lowestFareSingle.isFareFinal()) {
                if (lowestFare == null || lowestFareSingle.getTotalPrice() < lowestFare.getTotalPrice()) {
                    lowestFare = lowestFareSingle;
                }
            }
        }

        return lowestFare;
    }

    private static void writeToFile(String content) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getenv("RESULT_FILE")));
            writer.write(content);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
