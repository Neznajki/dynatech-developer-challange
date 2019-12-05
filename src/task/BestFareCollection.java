package task;

import java.util.*;

public class BestFareCollection {

    private static BestFareCollection instance;
    public static BestFareCollection getInstance ()
    {
        if (instance == null) {
            instance = new BestFareCollection();
        }

        return instance;
    }

    protected List<List<Fare>> faresList;

    public BestFareCollection()
    {
        Integer itinerarySize = Itinerary.getSize();
        this.faresList = new ArrayList<>();

        for(int i=0; i < itinerarySize; i++) {
            ArrayList<Fare> fares = new ArrayList<>();
            faresList.add(fares);
            int maxSize = itinerarySize - i;
            for(int k=0; k < maxSize; k++) {
                fares.add(null);
            }
        }
    }

    public List<Fare> getFaresByPosition(int position) {
        return faresList.get(position);
    }

    public void fareFound(data.object.Fare fare)
    {
        int itinerarySize = Itinerary.getSize();

        int flightLength = fare.getRoutes().length;
        if (flightLength > itinerarySize) {
            return;
        }

        Fare tempFare = this.convertFare(fare);
        flightLength--;

        for (int i = 0; i < itinerarySize; i++) {
            if (this.containsFlight(i, tempFare)) {
                List<Fare> positionFares = this.faresList.get(i);
                Fare currentFare = positionFares.get(flightLength);

                if (currentFare == null) {
                    positionFares.set(flightLength, tempFare);
                    return;
                }

                if (currentFare.price > tempFare.price) {
                    positionFares.set(flightLength, tempFare);
                    tempFare = currentFare;
                }
            }
        }
    }

    public void removeNullsAndReverse()
    {
        for (List<Fare> fares: this.faresList) {
            fares.removeIf(Objects::isNull);
            Collections.reverse(fares);
        }
    }

    protected Fare convertFare(data.object.Fare fare)
    {
        Fare result = new Fare();

        result.fid = fare.getFid();
        result.price = fare.getPrice();
        result.setFlights(fare.getRoutes());

        return result;
    }

    protected boolean containsFlight(int i, Fare fare) {
        Integer itinerarySize = Itinerary.getSize();

        int recordsLeft = itinerarySize - i;
        if (recordsLeft < fare.getFlightSize()) {
            return false;
        }

        for (int k = 0; k < fare.getFlightSize(); k++) {
            Flight ItineraryFlight = Itinerary.getInstance().getCollection().get(i + k);
            Flight flightDataFlight = fare.getFlight().get(k);

            if (!ItineraryFlight.equals(flightDataFlight)) {
                return false;
            }
        }

        return true;
    }
}
