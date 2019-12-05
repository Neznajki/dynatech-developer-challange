package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Fare;
import helper.Debug;
import task.FileData;
import task.Itinerary;
import task.PriceCollectionTerminator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Reader {

	public void getDataObjectGson(String filePath) throws Exception
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Fare.class, new FareDeserializerGson());
		gsonBuilder.registerTypeAdapter(Itinerary.class, new ItineraryDeserializer());
		Gson gson = gsonBuilder.create();

		PriceCollectionTerminator.fileInputStream = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.US_ASCII);

		try {
			gson.fromJson(PriceCollectionTerminator.fileInputStream, FileData.class);
		} catch (Exception e) { if (Debug.isDebug) {e.printStackTrace();} }

		Debug.debugTrace();
	}

	public void getDataObjectJackson(String filePath) throws Exception
	{

//		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.registerTypeAdapter(Fare.class, new FareDeserializerGson());
//		gsonBuilder.registerTypeAdapter(Itinerary.class, new ItineraryDeserializer());
//		Gson gson = gsonBuilder.create();

		PriceCollectionTerminator.fileInputStream = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.US_ASCII);
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Fare.class, new FareDeserializerJackson());
		module.addDeserializer(Itinerary.class, new ItineraryDeserializerJackson());
		mapper.registerModule(module);

		try {

			FileData gg = mapper.readValue(PriceCollectionTerminator.fileInputStream, FileData.class);

//			gson.fromJson(PriceCollectionTerminator.fileInputStream, FileData.class);
		} catch (Exception e) { if (Debug.isDebug) {e.printStackTrace();} }

		Debug.debugTrace();
	}

	public String convertObjectArrayToJsonString(String[] data)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(data);
	}
}
