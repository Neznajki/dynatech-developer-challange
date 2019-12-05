package generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.object.Fare;
import data.object.FileData;
import data.object.Flight;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
    public static void main(String[] args) throws IOException {
        Flight[] supported = new Flight[] {
                new Flight("AAA", "CCC"),
                new Flight("AAA", "BBB"),
                new Flight("DDD", "BBB"),
                new Flight("GGG", "BBB"),
                new Flight("AAA", "VVV"),
                new Flight("VVV", "BBB"),
                new Flight("AAA", "SSS"),
                new Flight("SSS", "BBB"),
                new Flight("AAA", "XXX"),
                new Flight("XXX", "BBB")
        };


        FileData fileData = new FileData();
        int itinerarySize = 100;
        int faresSize = 500000;
        fileData.fares = new Fare[faresSize];


        fileData.itinerary = getFlightArray(supported, itinerarySize);
        addFares(fileData, supported, faresSize, itinerarySize);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        FileWriter writer = new FileWriter(String.format("/work/java/dynatech-challange/src/generated_%d_%d.json", itinerarySize, faresSize));
        gson.toJson(fileData, writer);
        writer.flush();
        writer.close();
//        /work/java/dynatech-challange/src/example-data_league-5-big-path.json
//        supported.ad = new FlightFactory();
    }

    private static void addFares(FileData fileData, Flight[] supported, int count, int itinerarySize)
    {
        Random generator = new Random();

        for(int i = 0; i<count;i++) {
            Fare fare = new Fare();

            fare.setPrice(generator.nextInt(999999));
            fare.setFid(String.valueOf(i + 1));
            fare.setRoutes(getFlightArray(supported, generator.nextInt(itinerarySize - 1)));

            fileData.fares[i] = fare;
        }
    }

    private static Flight[] getFlightArray(Flight[] supported, int count)
    {
        Flight[] result = new Flight[count];

        for(int i=0; i< count; i++) {
            Random generator = new Random();
            int randomIndex = generator.nextInt(supported.length);
            result[i] = supported[randomIndex];
        }

        return result;
    }
}
