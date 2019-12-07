package task;

import java.util.ArrayList;
import java.util.List;

public class Fare implements Cloneable, Comparable<Fare> {
    public List<Flight> flights;
    public Integer price;
    public Integer fid;
    public Integer flightSize;
    public Integer pricePerFlight;
    public boolean usedForCalculations = false;

    public List<Flight> getFlight() {
        return this.flights;
    }

    public void setFlights(List<data.object.Flight> routes) {
        if (BestFareCollection.priceSearchLength == 0) {
            this.flights = FlightFactory.flightCollection(routes);
            return;
        }

        this.flights = new ArrayList<>();

        for (data.object.Flight flight: routes) {
            Flight taskFlight = FlightFactory.createFlight(flight.getFrom(), flight.getTo());
            this.flights.add(taskFlight);
        }
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getFid() {
        return fid;
    }

    public Integer getFlightSize() {
        if (flightSize == null) {
            this.flightSize = this.getFlight().size();
        }

        return flightSize;
    }

    public Integer getPricePerFlight() {
        if (pricePerFlight == null) {
            calculatePricePerFlight();
        }
        return pricePerFlight;
    }

    public void calculatePricePerFlight()
    {
        this.pricePerFlight = Math.round(this.getPrice() / this.getFlightSize());
    }

    @Override
    public int compareTo(Fare fare) {
        return this.getPricePerFlight() - fare.getPricePerFlight();
    }
}

