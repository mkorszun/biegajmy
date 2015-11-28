package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class NewUser implements Serializable {
    @SerializedName("email") public String email;
    @SerializedName("password") public String password;
}
