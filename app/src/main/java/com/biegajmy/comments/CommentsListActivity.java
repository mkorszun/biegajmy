package com.biegajmy.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.model.Comment;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_comments_list) public class CommentsListActivity extends ActionBarActivity {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = getIntent();
        String eventID = it.getStringExtra(EVENT_ID_ARG);
        List<Comment> comments = (List<Comment>) it.getSerializableExtra(COMMENTS_ARG);

        Fragment fr = CommentsListFragment_.builder()
            .arg(CommentsListFragment.EVENT_ID_ARG, eventID)
            .arg(CommentsListFragment.COMMENTS_ARG, new ArrayList(comments))
            .build();

        getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, fr).commit();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
