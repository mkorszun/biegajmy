package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Comment implements Serializable {

    @SerializedName("user_id") public String userID;
    @SerializedName("msg") public String msg;
    @SerializedName("date") public long timestamp;
    @SerializedName("photo_url") public String photoURL;
}
