package task;

import java.util.ArrayList;
import java.util.List;


public class Itinerary {
    protected static Integer size;
    protected static Itinerary instance;

    public static Integer getSize() {
        return size;
    }

    public static Itinerary getInstance()
    {
        return instance;
    }

    public List<Flight> collection;
    protected List<List<Flight>> supportedLongFLights;

    public Itinerary(List<Flight> collection) {
        this.collection = collection;
		size = collection.size();
        instance = this;

        makeLongFlight(collection, 0);
        makeLongFlight(collection, size - 1);
    }

    private void makeLongFlight(List<Flight> collection, int index) {
        ArrayList<Flight> flightCollection = new ArrayList<>(collection.size());
        flightCollection.addAll(collection);
        flightCollection.remove(index);
        FlightFactory.addExistingLongFlight(flightCollection);
    }

    public List<Flight> getCollection() {
        return collection;
    }
}
