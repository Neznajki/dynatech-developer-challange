package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import helper.Debug;
import task.AllFareCollector;


public class FareDeserializerTask implements Runnable {
    protected static ObjectMapper mapper = new ObjectMapper();
    private String context;

    public FareDeserializerTask(String context)
    {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            data.object.Fare fare = mapper.readValue(this.context, data.object.Fare.class);

            AllFareCollector.getInstance().fareFound(fare);
        } catch (Exception e) {
            if (Debug.isDebug) { e.printStackTrace(); }
        }
    }
}
