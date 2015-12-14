package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id") public String id;
    @SerializedName("first_name") public String firstName = "";
    @SerializedName("last_name") public String lastName = "";
    @SerializedName("photo_url") public String photo_url;

    @SerializedName("bio") public String bio = "";
    @SerializedName("telephone") public String telephone = "";
    @SerializedName("www") public String www = "";
    @SerializedName("email") public String email = "";

    @SerializedName("settings") public UserSettings settings = new UserSettings();

    @Override public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            return user.id.equals(this.id);
        }
        return false;
    }

    @Override public String toString() {
        return lastName != null ? String.format("%s %s", firstName, lastName) : String.format("%s", firstName);
    }

    @Override public int hashCode() {
        return firstName.hashCode()
            + lastName.hashCode()
            + bio.hashCode()
            + telephone.hashCode()
            + www.hashCode()
            + email.hashCode()
            + settings.hashCode();
    }
}
