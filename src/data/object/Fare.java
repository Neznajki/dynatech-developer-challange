package data.object;

import com.google.gson.annotations.SerializedName;

public class Fare {
    @SerializedName("routes")
    public Flight[] routes;
    @SerializedName("price")
    public Integer price;
    @SerializedName("fid")
    public String fid;

    public void setRoutes(Flight[] routes) {
        this.routes = routes;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Flight[] getRoutes() {
        return routes;
    }

    public Integer getPrice() {
        return price;
    }

    public String getFid() {
        return fid;
    }
}
