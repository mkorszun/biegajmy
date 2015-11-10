package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    @SerializedName("_id") public String id;
    @SerializedName("user") public User user;
    @SerializedName("timestamp") public long timestamp;
    @SerializedName("headline") public String headline;
    @SerializedName("description") public String description;
    @SerializedName("participants") public ArrayList<User> participants = new ArrayList<>();
    @SerializedName("loc") public EventLocation location;
    @SerializedName("tags") public List<String> tags;
    @SerializedName("distance") public int distance;
    @SerializedName("pace") public double pace;
    @SerializedName("deleted") public boolean deleted;
    @SerializedName("spots") public int spots;
    @SerializedName("comments") public List<Comment> comments = new ArrayList<>();

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
