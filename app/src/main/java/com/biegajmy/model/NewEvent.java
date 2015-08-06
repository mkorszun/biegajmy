package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class NewEvent implements Serializable {
    @SerializedName("date_and_time") public String dateAndTime;
    @SerializedName("headline") public String headline;
    @SerializedName("description") public String description;
    @SerializedName("x") public double x;
    @SerializedName("y") public double y;
    @SerializedName("tags") public List<String> tags;
    @SerializedName("distance") public int distance;
    @SerializedName("pace") public double pace;
}
