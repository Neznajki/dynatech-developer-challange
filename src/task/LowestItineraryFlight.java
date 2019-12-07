package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LowestItineraryFlight {
    private List<Boolean> positions;
    private List<Fare> fareCollection = new ArrayList<>();
    private Integer totalPrice;

    public LowestItineraryFlight(Fare fare)
    {
        positions = new ArrayList<>(Collections.nCopies(Itinerary.getSize(), false));
        this.addFare(fare);
    }

    public boolean isFareFinal()
    {
        for (boolean havePos :this.positions) {
            if (! havePos) {
                return false;
            }
        }

        return true;
    }

    public boolean addFare(Fare fare)
    {
        Integer itinerarySize = Itinerary.getSize();
        Integer flightSize = fare.getFlightSize();

        for (int i = 0; i < itinerarySize; i++) {
            if (itinerarySize - i < flightSize) {
                break;
            }

            if (this.positions.get(i)) {
                continue;
            }

            if (this.canSuiteItinerary(fare, i)) {
                this.addFareToCollection(fare, i);

                return true;
            }
        }

        return false;
    }

    public int getTotalPrice()
    {
        if (this.totalPrice == null) {
            this.totalPrice = 0;
            for (Fare fare:this.fareCollection) {
                this.totalPrice += fare.getPrice();
            }
        }

        return this.totalPrice;
    }

    public int size()
    {
        int result = 0;

        for (int i=0;i<this.fareCollection.size();i++) {
            result += this.fareCollection.get(i).getFlightSize();
        }

        return result;
    }

    public String[] getFreIds() {
        String[] result = new String[this.fareCollection.size()];

        int i = 0;
        for (Fare fare : this.fareCollection) {
            result[i] = String.valueOf(fare.getFid());
            i++;
        }

        return result;
    }

    private boolean canSuiteItinerary(Fare fare, int position)
    {
        Integer flightSize = fare.getFlightSize();

        for (int i = 0; i < flightSize; i++) {
            if (this.positions.get(position + i)) {
                return false;
            }
            List<Flight> itinerary = Itinerary.getInstance().getCollection();
            Flight itineraryFlight = itinerary.get(position + i);

            if (itineraryFlight != fare.getFlight().get(i)) {
                return false;
            }
        }

        return true;
    }

    private void addFareToCollection(Fare fare, int position)
    {
        fare.usedForCalculations = true;
        this.fareCollection.add(fare);

        Integer flightSize = fare.getFlightSize();
        for (int i = 0; i < flightSize; i++) {
            this.positions.set(position + i, true);
        }
    }
}
