package task;

//import org.json.JSONArray;
//import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class FareListFactory {
//	public static List<Fare> create(JSONArray jsonArray) {
//		List<Fare> collection = new ArrayList<Fare>();
//
//		for (Object singleEntry : jsonArray) {
//			try {
//				JSONObject jsonObject = (JSONObject) singleEntry;
//
//				if (!jsonObject.has("routes")) {
//					throw new InvalidParameterException("json object have no routes");
//				}
//
//				if (!jsonObject.has("price")) {
//					throw new InvalidParameterException("json object have no price");
//				}
//
//				if (!jsonObject.has("fid")) {
//					throw new InvalidParameterException("json object have no fid");
//				}
//
//				List<Flight> flights = FlightListFactory.create((JSONArray) jsonObject.get("routes"));
//				collection.add(
//						new Fare(
//								flights,
//								jsonObject.getInt("price"),
//								jsonObject.getString("fid")
//						)
//				);
//			} catch (Exception e) {
//			}
//		}
//
//		return collection;
//	}
}
