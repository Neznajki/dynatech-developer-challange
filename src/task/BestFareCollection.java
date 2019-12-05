package task;

import java.util.*;

public class BestFareCollection extends AbstractFareCollector
{

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

    @Override
    protected Fare handleValidFare(Fare tempFare, int flightLength, int i) {
        List<Fare> positionFares = this.faresList.get(i);
        Fare currentFare = positionFares.get(flightLength);

        if (currentFare == null) {
            positionFares.set(flightLength, tempFare);
            return null;
        }

        if (currentFare.price > tempFare.price) {
            positionFares.set(flightLength, tempFare);
            tempFare = currentFare;
        }
        return tempFare;
    }

    @Override
    protected boolean requiresDuplicates() {
        return true;
    }

    public void removeNullsAndReverse()
    {
        for (List<Fare> fares: this.faresList) {
            fares.removeIf(Objects::isNull);
            Collections.reverse(fares);
        }
    }
}
