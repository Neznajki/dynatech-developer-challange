package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Fare;
import helper.Debug;
import task.FileData;
import task.Itinerary;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Reader {

	public void getDataObject(String filePath) throws Exception
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Fare.class, new FareDeserializer());
		gsonBuilder.registerTypeAdapter(Itinerary.class, new ItineraryDeserializer());
		Gson gson = gsonBuilder.create();

		java.io.Reader streamReader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.US_ASCII);

		gson.fromJson(streamReader, FileData.class);

		Debug.debugTrace();
	}


	public String convertObjectArrayToJsonString(String[] data)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(data);
	}
}
