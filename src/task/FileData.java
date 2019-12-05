package task;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileData {
	@SerializedName("itinerary")
	public Itinerary itineraryObject;
	public List<Fare> fares;

	public Itinerary getItinerary() {
		return itineraryObject;
	}

	public List<Fare> getFares() {
		return fares;
	}
}
