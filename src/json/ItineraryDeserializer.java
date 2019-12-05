package json;

import com.google.gson.*;
import task.Flight;
import task.FlightFactory;
import task.Itinerary;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDeserializer implements JsonDeserializer<Itinerary> {
    @Override
    public Itinerary deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = new Gson();
        Flight[] flights = gson.fromJson(jsonElement, Flight[].class);

        List<Flight> flightList = new ArrayList<>();

        for (Flight flight: flights) {
            flightList.add(FlightFactory.create(flight));
        }

        return new Itinerary(flightList);
    }
}
