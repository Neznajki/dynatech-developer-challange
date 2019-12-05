package task;

import helper.Debug;

import java.util.List;

public class LowestPriceGatherTask implements Runnable {
    public static Integer fareCount = 100;
    public static Integer faresCalculated = 0;
    public static LowestPriceGatherTask nextFlightSearchTask;
    public static LowestItineraryFlight lowestItineraryFlight;
    private LowestItineraryFlight itineraryFlight;

    public LowestPriceGatherTask(LowestItineraryFlight itineraryFlight) {
        this.itineraryFlight = itineraryFlight;
        faresCalculated++;
        nextFlightSearchTask = null;
    }

    @Override
    public void run() {
        nextFlightSearchTask = null;
        Integer itinerarySize = Itinerary.getSize();
        if (itinerarySize.equals(1)) {
            nextFlightSearchTask = null;

            return;
        }

        try {
            List<Fare> fareCollection = AllFareCollector.getInstance().getCollection();
            for (int i = 1; i < fareCollection.size(); i++) {
                if (TaskExecutor.terminateTask && lowestItineraryFlight != null) {
                    nextFlightSearchTask = null;
                    return;
                }
                Fare fare = fareCollection.get(i);
                if (! this.itineraryFlight.addFare(fare)) {
                    if (! fare.usedForCalculations && fareCount >= faresCalculated && nextFlightSearchTask == null) {
                        nextFlightSearchTask = new LowestPriceGatherTask(new LowestItineraryFlight(fare));
                    }

                    continue;
                }

                if (this.itineraryFlight.isFareFinal()) {
                    if (nextFlightSearchTask == this) {
                        nextFlightSearchTask = null;
                    }

                    if (lowestItineraryFlight == null) {
                        lowestItineraryFlight = this.itineraryFlight;
                    } else if (lowestItineraryFlight.getTotalPrice() > this.itineraryFlight.getTotalPrice()) {
                        lowestItineraryFlight = this.itineraryFlight;
                    }

                    return;
                }
            }
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace();}
        }

        if (nextFlightSearchTask == this) {
            nextFlightSearchTask = null;
        }
    }
}
