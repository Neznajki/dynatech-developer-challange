package task;

//import org.json.JSONArray;
//import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class FlightListFactory {
//	protected static ArrayList<String> locations = new ArrayList<String>();
//
//	public static List<Flight> create(JSONArray jsonArray)
//	{
//		List<Flight> collection = new ArrayList<Flight>();
//
//		for(Object singleEntry : jsonArray) {
//			JSONObject jsonObject = (JSONObject) singleEntry;
//
//			if (! jsonObject.has("from")) {
//				throw new InvalidParameterException("json object have no from");
//			}
//
//			if (! jsonObject.has("to")) {
//				throw new InvalidParameterException("json object have no to");
//			}
//
//			String
//				from = jsonObject.getString("from"),
//				to = jsonObject.getString("to");
//
//			for (String location : locations) {
//				if (from.equals(location)) {
//					from = location;
//				}
//
//				if (to.equals(location)) {
//					to = location;
//				}
//			}
//
//			if (! locations.contains(from)) {
//				locations.add(from);
//			}
//
//			if (! locations.contains(to)) {
//				locations.add(to);
//			}
//
//			collection.add(new Flight(
//					from,
//					to
////					jsonObject.getString("from"),
////					jsonObject.getString("to")
//			));
//		}
//
//		return collection;
//	}
}
