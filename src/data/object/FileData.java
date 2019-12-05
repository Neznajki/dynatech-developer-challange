package data.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileData {
    @SerializedName("itinerary")
    public Flight[] itinerary;
    @SerializedName("fares")
    public Fare[] fares;
}
