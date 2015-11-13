package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserSettings implements Serializable {
    @SerializedName("push_on_new_comment") public boolean onNewComment = true;
    @SerializedName("push_on_new_participant") public boolean onNewParticipant = true;
    @SerializedName("push_on_update") public boolean onUpdate = true;
    @SerializedName("push_on_leaving_participant") public boolean onLeavingParticipant = true;
}
