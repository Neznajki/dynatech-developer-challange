package task;

import java.util.ArrayList;
import java.util.List;

public class FlightFactory {
    protected static ArrayList<Flight> flights = new ArrayList<>();
    protected static ArrayList<ArrayList<Flight>> flightLists = new ArrayList<>();
    protected static ArrayList<ArrayList<Flight>> longFlightsList = new ArrayList<>();

    public static Flight getFlight(Flight flight) {
        return getFlight(flight.getFrom(), flight.getTo());
    }

    public static Flight getFlight(data.object.Flight flight) {
        return getFlight(flight.getFrom(), flight.getTo());
    }

    private static Flight getFlight(String from, String to) {
        for (Flight flight : flights) {
            if (flight.getFrom().equals(from) && flight.getTo().equals(to)) {
                return flight;
            }
        }
        return null;
    }

    public static Flight createFlight(String from, String to) {
        Flight flight = getFlight(from, to);
        if (flight == null) {
            flight = new Flight();
            flight.from = from;
            flight.to = to;

            flights.add(flight);
        }


        return flight;
    }

    public static void addExistingLongFlight(ArrayList<Flight> flights)
    {
        longFlightsList.add(flights);
    }

    public static List<Flight> flightCollection(List<data.object.Flight> flight)
    {
        int size = flight.size();
        if (size == 1) {
            return getSingleDestinationFlightList(flight);
        }

        List<Flight> converted = null;
        for(int i=0;i<longFlightsList.size();i++) {
            ArrayList<Flight> existingLongFlight = longFlightsList.get(i);

            converted = convertFlight(flight, existingLongFlight);

            if (converted != null) {
                break;
            }
        }

        return converted;
    }

    private static List<Flight> convertFlight(List<data.object.Flight> flight, ArrayList<Flight> existingLongFlight) {
        for(int k=0; k< existingLongFlight.size(); k++) {
            if (getFlight(flight.get(k)) != existingLongFlight.get(k)) {
                return null;
            }
        }
        return existingLongFlight;
    }

    private static ArrayList<Flight> getSingleDestinationFlightList(List<data.object.Flight> flight) {
        Flight newFlight = getFlight(flight.get(0));
        if (newFlight == null) {
            return null;
        }
        for (int i=0; i < flightLists.size(); i++) {
            ArrayList<Flight> flights = flightLists.get(i);
            if (flights.get(0) == newFlight) {
                return flights;
            }
        }

        ArrayList<Flight> newFlightCollection = new ArrayList<>(1);
        newFlightCollection.add(newFlight);
        flightLists.add(newFlightCollection);

        return newFlightCollection;
    }
}
