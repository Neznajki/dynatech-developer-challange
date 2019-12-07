package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Fare;
import helper.Debug;
import task.FileData;
import task.Itinerary;
import java.io.*;

public class Reader {

	public void collectObjectData(String filePath) throws Exception
	{

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Fare.class, new FareDeserializerJackson());
		module.addDeserializer(Itinerary.class, new ItineraryDeserializerJackson());
		mapper.registerModule(module);

		try {
			mapper.readValue(new File(filePath), FileData.class);
		} catch (Exception e) { if (Debug.isDebug) {e.printStackTrace();} }

		Debug.debugTrace();
	}

	public String convertObjectArrayToJsonString(String[] data)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(data);
	}
}
