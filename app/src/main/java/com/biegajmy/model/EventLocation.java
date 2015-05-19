package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class EventLocation implements Serializable {
    @SerializedName("type") public String type;
    @SerializedName("coordinates") public List<Double> coordinates;
}
