package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.gson.*;
import task.Flight;
import task.FlightFactory;
import task.Itinerary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDeserializerJackson extends JsonDeserializer<Itinerary> {

    @Override
    public Itinerary deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Gson gson = new Gson();
        Flight[] flights = gson.fromJson(jsonParser.readValueAsTree().toString(), Flight[].class);

        List<Flight> flightList = new ArrayList<>();

        for (Flight flight: flights) {
            flightList.add(FlightFactory.createFlight(flight));
        }

        return new Itinerary(flightList);
    }
}
