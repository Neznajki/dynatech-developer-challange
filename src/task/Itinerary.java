package task;

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

    public Itinerary(List<Flight> collection) {
        this.collection = collection;
		size = collection.size();
        instance = this;
    }

    public List<Flight> getCollection() {
        return collection;
    }
}
