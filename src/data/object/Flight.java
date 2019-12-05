package data.object;

import com.google.gson.annotations.SerializedName;

public class Flight {
    @SerializedName("from")
    public String from;
    @SerializedName("to")
    public String to;

    public Flight()
    {

    }

    public Flight(String from, String to)
    {
        this.from = from.intern();
        this.to = to.intern();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
