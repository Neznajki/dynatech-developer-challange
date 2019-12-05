package json;

import com.google.gson.*;
import helper.Debug;
import task.AllFareCollector;
import task.Fare;

import java.lang.reflect.Type;

public class FareDeserializerGson implements JsonDeserializer<Fare> {
    protected static int counter = 0;
    protected static long callCount = 0;
    protected static Gson gson = new Gson();

    @Override
    public Fare deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        callCount++;
        counter++;
        if (counter == 10000) {
            counter = 0;
            if (Debug.isDebug) {System.out.println(callCount);}
        }
        try {
            data.object.Fare fare = gson.fromJson(jsonElement, data.object.Fare.class);
            AllFareCollector.getInstance().fareFound(fare);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace(); }
        }

        return null;
    }
}
