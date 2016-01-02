package com.biegajmy.comments;

import com.biegajmy.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentsUtils {

    public static final int COMMENTS_LIMIT = 3;

    public static ArrayList<Comment> getLast(List<Comment> comments) {
        int size = comments.size();
        if (size > COMMENTS_LIMIT) {
            int start = comments.size() - COMMENTS_LIMIT;
            return new ArrayList<>(comments.subList(start, comments.size()));
        } else {
            return new ArrayList<>(comments);
        }
    }
}
