package task;

import java.util.*;

public class BestFareCollection extends AbstractFareCollector
{
    public static Integer priceSearchLength = 2;
    private static BestFareCollection instance;
    public static BestFareCollection getInstance ()
    {
        if (instance == null) {
            instance = new BestFareCollection();
        }

        return instance;
    }

    private Integer minRequiredLength;
    protected List<List<Fare>> faresList;

    public BestFareCollection()
    {
        Integer itinerarySize = Itinerary.getSize();
        this.faresList = new ArrayList<>();
        this.minRequiredLength = itinerarySize - priceSearchLength;

        for(int i=0; i < itinerarySize; i++) {
            ArrayList<Fare> fares = new ArrayList<>();
            faresList.add(fares);
            int maxSize = itinerarySize - i;
            for(int k=0; k < maxSize; k++) {
                fares.add(null);
            }
        }
    }

    public List<List<Fare>> getFaresList() {
        return faresList;
    }

    public List<Fare> getFaresByPosition(int position) {
        return faresList.get(position);
    }

    public void removeNullsAndReverse()
    {
        Iterator<List<Fare>> iterator = this.faresList.iterator();
        while (iterator.hasNext()) {
            List<Fare> fares = iterator.next();
            fares.removeIf(Objects::isNull);

            if (fares.size() == 0) {
                iterator.remove();

                continue;
            }

            Collections.reverse(fares);
        }

        System.out.println(String.format("done sorting %d", this.faresList.size()));
    }

    public List<Fare> getFaresCollection()
    {
        this.removeNullsAndReverse();
        List<Fare> result = new ArrayList<>();

        for (List<Fare> fares: this.faresList) {
            result.addAll(fares);
        }

        return result;
    }

    @Override
    protected boolean isFareSuitable(data.object.Fare fare) {
        if (priceSearchLength == 0) {
            return true;
        }

        return fare.routes.size() < priceSearchLength || fare.routes.size() > this.minRequiredLength;
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
}
