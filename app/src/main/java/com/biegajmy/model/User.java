package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

    @SerializedName("id") public String id;
    @SerializedName("first_name") public String firstName = "";
    @SerializedName("last_name") public String lastName = "";
    @SerializedName("photo_url") public String photo_url;

    @SerializedName("bio") public String bio = "";
    @SerializedName("telephone") public String telephone = "";
    @SerializedName("www") public String www = "";
    @SerializedName("email") public String email = "";

    @SerializedName("tags") public List<String> tags = Collections.emptyList();

    @Override public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            return user.id.equals(this.id);
        }
        return false;
    }
}
