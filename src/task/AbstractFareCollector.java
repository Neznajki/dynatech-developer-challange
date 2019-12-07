package task;

abstract public class AbstractFareCollector {

    public boolean fareFound(data.object.Fare fare)
    {
        if (fare.routes.size() == 0) {
            return true;
        }

        if (! this.isFareSuitable(fare)) {
            return false;
        }

        int itinerarySize = Itinerary.getSize();
        Fare tempFare = this.convertFare(fare);

        if (tempFare.flights == null) {
            return true;
        }

        int flightLength = fare.getRoutes().size();
        if (flightLength > itinerarySize) {
            return true;
        }

        flightLength--;
        boolean result = false;
        for (int i = 0; i < itinerarySize; i++) {

            if (this.containsFlight(i, tempFare)) {
                result = true;
                tempFare = handleValidFare(tempFare, flightLength, i);
                if (! this.requiresDuplicates() || tempFare == null) {
                    return true;
                }
            }
        }

        return result;
    }

    protected abstract boolean isFareSuitable(data.object.Fare fare);
    protected abstract Fare handleValidFare(Fare tempFare, int flightLength, int i);
    protected abstract boolean requiresDuplicates();

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

            if (ItineraryFlight != flightDataFlight) {
                return false;
            }
        }

        return true;
    }
}
