package ots.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {
    private final int eventId;
    private final String userEmail;
    private final double price;
    private final String location;

    @JsonCreator
    public Ticket(@JsonProperty("eventId") int eventId, @JsonProperty("userEmail") String userEmail, @JsonProperty("price") double price, @JsonProperty("location") String location) {
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.price = price;
        this.location = location;
    }

    public int getEventId() {
        return eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }
}
