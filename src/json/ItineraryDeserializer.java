package json;

import com.google.gson.*;
import task.Flight;
import task.Itinerary;

import java.lang.reflect.Type;
import java.util.Arrays;

public class ItineraryDeserializer implements JsonDeserializer<Itinerary> {
    @Override
    public Itinerary deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = new Gson();
        Flight[] flights = gson.fromJson(jsonElement, Flight[].class);

        return new Itinerary(Arrays.asList(flights));
    }
}
