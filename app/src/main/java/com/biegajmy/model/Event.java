package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

    @SerializedName("_id") public String id;
    @SerializedName("user") public User user;
    @SerializedName("timestamp") public long timestamp;
    @SerializedName("headline") public String headline;
    @SerializedName("description") public String description;
    @SerializedName("loc") public EventLocation location;
    @SerializedName("distance") public int distance;
    @SerializedName("pace") public double pace;
    @SerializedName("spots") public int spots;
    @SerializedName("official") public boolean official;

    @SerializedName("tags") public ArrayList<String> tags = new ArrayList<>();
    @SerializedName("comments") public ArrayList<Comment> comments = new ArrayList<>();
    @SerializedName("participants") public ArrayList<User> participants = new ArrayList<>();

    public static Event build(String headline, String description, String id) {
        Event event = new Event();
        event.id = id;
        event.headline = headline;
        event.description = description;
        return event;
    }

    @Override public boolean equals(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o;
            return event.id.equals(this.id);
        }

        return false;
    }
}
