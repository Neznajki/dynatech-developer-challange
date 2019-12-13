package task;

import concurrency.Worker;
import helper.Debug;

import java.util.List;

public class LowestPriceGatherTask implements Runnable {
    public static Integer fareCount = 3;
    public static Integer faresCalculated = 0;
    public static LowestItineraryFlight lowestItineraryFlight;
    private LowestItineraryFlight itineraryFlight;

    public LowestPriceGatherTask(LowestItineraryFlight itineraryFlight) {
        this.itineraryFlight = itineraryFlight;
//        faresCalculated++;
    }

    @Override
    public void run() {
        Integer itinerarySize = Itinerary.getSize();
        if (itinerarySize.equals(1)) {
            return;
        }

        try {
            List<Fare> fareCollection = AllFareCollector.getInstance().getCollection();
            int size = fareCollection.size();
            for (int i = 1; i < size; i++) {
                Fare fare = fareCollection.get(i);
                if (! this.itineraryFlight.addFare(fare)) {
                    if (! fare.usedForCalculations && fareCount >= faresCalculated) {
                        Worker.addTask(new LowestPriceGatherTask(new LowestItineraryFlight(fare)));
                    }

                    continue;
                }

                if (this.itineraryFlight.isFareFinal()) {
                    if (lowestItineraryFlight == null) {
                        lowestItineraryFlight = this.itineraryFlight;
                    } else if (lowestItineraryFlight.getTotalPrice() > this.itineraryFlight.getTotalPrice()) {
                        lowestItineraryFlight = this.itineraryFlight;
                    }

                    Worker.taskDone = true;
                    return;
                }
            }
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace();}
        }
    }
}
