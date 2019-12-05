package task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileData {
	public Itinerary itinerary;
	public List<Fare> fares;

	public Itinerary getItinerary() {
		return itinerary;
	}

	public List<Fare> getFares() {
		return fares;
	}
}
