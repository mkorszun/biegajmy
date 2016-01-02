package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class NewEvent implements Serializable {
    @SerializedName("timestamp") public long timestamp;
    @SerializedName("headline") public String headline;
    @SerializedName("description") public String description;
    @SerializedName("x") public double x;
    @SerializedName("y") public double y;
    @SerializedName("tags") public List<String> tags;
    @SerializedName("distance") public double distance;
    @SerializedName("pace") public Double pace;
}
