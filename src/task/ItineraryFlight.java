package task;

import helper.Debug;
import helper.Formatter;

import java.util.ArrayList;
import java.util.Iterator;

public class ItineraryFlight implements Cloneable {
    protected static ArrayList<ItineraryFlight> existingFlights = new ArrayList<>();
    protected static ItineraryFlight minPriced = null;

    public static synchronized void addFinalPrice(ItineraryFlight flight)
    {
        existingFlights.add(flight);

        removeNotAcceptableFares();
    }

    public static ItineraryFlight createInstance(Fare firstFare) {
        ItineraryFlight result = new ItineraryFlight(firstFare);

        existingFlights.add(result);

        return result;
    }

    public static ItineraryFlight getDeepSearchInstance(ItineraryFlight currentFlight, Fare nextFare) throws CloneNotSupportedException {
        ItineraryFlight result = currentFlight.deepCLone();

        result.addAdditionalFare(nextFare);

        return result;
    }

    private static void traceDebugInfo() {
        if (Debug.isDebug) {
            System.out.println(Runtime.getRuntime().availableProcessors());
            System.out.println(Formatter.formatSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
            System.out.println(Formatter.formatSize(Runtime.getRuntime().totalMemory()));
            System.out.println(Formatter.formatSize(Runtime.getRuntime().maxMemory()));
            System.out.println(existingFlights.size());
        }
    }

    public static synchronized void removeNotAcceptableFares() {
//        traceDebugInfo();

        ArrayList<ItineraryFlight> pendingRemove = new ArrayList<>();
        Iterator<ItineraryFlight> iterator = existingFlights.iterator();      // it will return iterator
        while (iterator.hasNext()) {
            ItineraryFlight itineraryFlight = iterator.next();

            if (!itineraryFlight.isFareFinal()) {
                continue;
            }

            if (minPriced == null) {
                minPriced = itineraryFlight;

                continue;
            }

            if (itineraryFlight.getTotalCost() < minPriced.getTotalCost()) {
                if (Debug.isDebug) {
                    System.out.println(String.format("removing %d total price as found lower %d", minPriced.getTotalCost(), itineraryFlight.getTotalCost()));
                }
                pendingRemove.add(minPriced);

                minPriced = itineraryFlight;
                continue;
            }

            iterator.remove();
        }

        if (Debug.isDebug) {
           System.out.println(String.format("done cleanup %d removing elements", existingFlights.size()));
        }

        existingFlights.removeAll(pendingRemove);

        if (Debug.isDebug) {
            System.out.println("elements removed");
        }
    }

    public static ItineraryFlight getMinPriced() {
        return minPriced;
    }

    protected ArrayList<Fare> fares = new ArrayList<>();
    protected Integer totalCost = 0;
    protected boolean priceFinal = false;
    protected Integer position = 0;

    protected ItineraryFlight(Fare firstFare) {
        addFareToCollection(firstFare);
    }

    public void addAdditionalFare(Fare fare) {
        this.addFareToCollection(fare);
    }

    public void markPriceAsFinal() {
        this.priceFinal = true;
    }

    public boolean isFareFinal() {
        return this.priceFinal;
    }

    public Integer getCurrentTotalCost() {
        return this.totalCost;
    }

    public Integer getTotalCost() {
        if (! this.isFareFinal()) {
            throw new RuntimeException("can't calculate cost on not final fare");
        }

        return this.totalCost;
    }

    public Integer getPosition() {
        return position;
    }

    public String[] getFreIds() {
        String[] result = new String[this.fares.size()];

        int i = 0;
        for (Fare fare : this.fares) {
            result[i] = fare.getFid();
            i++;
        }

        return result;
    }

    protected void addFareToCollection(Fare fare) {
        this.fares.add(fare);
        this.position += fare.getFlightSize();
        this.totalCost += fare.getPrice();
    }

    protected ItineraryFlight deepCLone() throws CloneNotSupportedException {
        ItineraryFlight result = (ItineraryFlight) this.clone();

        result.fares = (ArrayList<Fare>) this.fares.clone();
        result.position = 0;
        for (Fare fare: result.fares) {
            result.position += fare.getFlightSize();
        }

        return result;
    }
}
