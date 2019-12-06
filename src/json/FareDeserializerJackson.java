package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import task.AllFareCollector;
import task.Fare;

import java.io.IOException;


public class FareDeserializerJackson extends JsonDeserializer<Fare> {
    protected static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Fare deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
//            ObjectCodec oc = jsonParser.getCodec();
//            JsonNode node = oc.readTree(jsonParser);
//
//            node.get("routes").
            data.object.Fare fare = mapper.readValue(jsonParser, data.object.Fare.class);

            AllFareCollector.getInstance().fareFound(fare);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
