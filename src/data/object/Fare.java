package data.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fare {
    public ArrayList<Flight> routes;
    public Integer price;
    public Integer fid;

    public ArrayList<Flight> getRoutes() {
        return routes;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getFid() {
        return fid;
    }
}
