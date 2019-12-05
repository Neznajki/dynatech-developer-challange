package data.object;

import com.google.gson.annotations.SerializedName;
import task.Flight;

public class Itinerary {
    @SerializedName("itinerary")
    public Flight[] flights;
}
