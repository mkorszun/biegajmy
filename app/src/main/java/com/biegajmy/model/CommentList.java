package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CommentList implements Serializable {

    @SerializedName("comments") public List<Comment> comments;
}
