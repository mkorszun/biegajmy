package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    @SerializedName("_id") public String id;
    @SerializedName("user") public User user;
    @SerializedName("spots") public int spots;
    @SerializedName("date_and_time") public String dateAndTime;
    @SerializedName("headline") public String headline;
    @SerializedName("cost") public double cost;
    @SerializedName("duration") public int duration;
    @SerializedName("description") public String description;
    @SerializedName("participants") public ArrayList<User> participants;
    @SerializedName("loc") public EventLocation location;
    @SerializedName("tags") public List<String> tags;

    public static Event build(String headline, String description) {
        Event event  = new Event();
        event.headline = headline;
        event.description = description;
        return event;
    }
}
