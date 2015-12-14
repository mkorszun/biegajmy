package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserSettings implements Serializable {
    @SerializedName("push_on_new_comment") public boolean onNewComment = true;
    @SerializedName("push_on_new_participant") public boolean onNewParticipant = true;
    @SerializedName("push_on_update") public boolean onUpdate = true;
    @SerializedName("push_on_leaving_participant") public boolean onLeavingParticipant = true;

    @Override public int hashCode() {
        int i = onNewComment ? 123 : 234;
        int i1 = onNewParticipant ? 345 : 456;
        int i2 = onUpdate ? 567 : 678;
        int i3 = onLeavingParticipant ? 789 : 890;
        return i + i1 + i2 + i3;
    }
}
