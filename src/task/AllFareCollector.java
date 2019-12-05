package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllFareCollector extends AbstractFareCollector {
    private static AllFareCollector instance;
    public static AllFareCollector getInstance()
    {
        if (instance == null) {
            instance = new AllFareCollector();
        }

        return instance;
    }

    protected List<Fare> collection = new ArrayList<>();

    public void prepare()
    {
        Collections.sort(collection);
    }

    public List<Fare> getCollection() {
        return collection;
    }

    @Override
    protected Fare handleValidFare(Fare tempFare, int flightLength, int i) {
        this.collection.add(tempFare);
        return null;
    }

    @Override
    protected boolean requiresDuplicates() {
        return false;
    }
}
