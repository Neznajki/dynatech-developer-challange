package json;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import helper.Debug;
import task.AllFareCollector;
import task.Fare;

import java.io.IOException;

public class FareDeserializerJackson extends JsonDeserializer<Fare> {
    protected static int counter = 0;
    protected static long callCount = 0;
    protected static Gson gson = new Gson();


    @Override
    public Fare deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

//        callCount++;
//        counter++;
//        if (counter == 10000) {
//            counter = 0;
//            if (Debug.isDebug) {System.out.println(callCount);}
//        }
        try {
            data.object.Fare fare = gson.fromJson(jsonParser.readValueAsTree().toString(), data.object.Fare.class);

            AllFareCollector.getInstance().fareFound(fare);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace(); }
        }

        return null;
    }
}
