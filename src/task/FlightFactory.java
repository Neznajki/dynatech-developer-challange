package task;

import java.util.ArrayList;

public class FlightFactory {
	protected static ArrayList<Flight> flights = new ArrayList<>();

	public static Flight create(Flight flight)
    {
        return create(flight.getFrom(), flight.getTo());
    }

	public static Flight create(String from, String to)
	{
        for (Flight flight: flights) {
            if (flight.getFrom().equals(from) && flight.getTo().equals(to)) {
                return flight;
            }
        }

        Flight newFlight = new Flight();
        newFlight.from = from.intern();
        newFlight.to = to.intern();

        flights.add(newFlight);

        return newFlight;
    }
}
