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
    @SerializedName("tags") public List<String> tags = Collections.emptyList();

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            return user.id.equals(this.id);
        }
        return false;
    }
}
