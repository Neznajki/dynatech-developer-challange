package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import task.BestFareCollection;
import task.Fare;

import java.io.IOException;
import java.time.LocalDateTime;


public class FareDeserializerJackson extends JsonDeserializer<Fare> {
    public static LocalDateTime began;
    public static boolean repeatRun = false;
    public static int counter;
    protected static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Fare deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            BestFareCollection.getInstance().fareFound(mapper.readValue(jsonParser, data.object.Fare.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
