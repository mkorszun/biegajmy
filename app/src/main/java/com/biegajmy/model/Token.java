package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Token implements Serializable {

    @SerializedName("id") public String id;
    @SerializedName("token") public String token;
}
