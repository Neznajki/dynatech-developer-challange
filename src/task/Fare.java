package task;

import java.util.ArrayList;
import java.util.List;

public class Fare implements Cloneable {
    public List<Flight> flights;
    public Integer price;
    public String fid;
    public Integer flightSize;

    public List<Flight> getFlight() {
        return this.flights;
    }

    public void setFlights(data.object.Flight[] routes) {
        this.flights = new ArrayList<>();

        for (data.object.Flight flight: routes) {
            Flight taskFlight = new Flight();
            taskFlight.from = flight.getFrom();
            taskFlight.to = flight.getTo();
            this.flights.add(taskFlight);
        }
    }

    public Integer getPrice() {
        return price;
    }

    public String getFid() {
        return fid;
    }

    public Integer getFlightSize() {
        if (flightSize == null) {
            this.flightSize = this.getFlight().size();
        }

        return flightSize;
    }
}

