package json;

import com.google.gson.*;
import task.BestFareCollection;
import task.Fare;

import java.lang.reflect.Type;

public class FareDeserializer implements JsonDeserializer<Fare> {
    protected static int counter = 0;
    protected static Gson gson = new Gson();

    @Override
    public Fare deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        counter++;
        if (counter == 10000) {
            System.gc();
        }
        try {
            data.object.Fare fare = gson.fromJson(jsonElement, data.object.Fare.class);
            BestFareCollection.getInstance().fareFound(fare);
        } catch (Exception e) {}

        return null;
    }
}
