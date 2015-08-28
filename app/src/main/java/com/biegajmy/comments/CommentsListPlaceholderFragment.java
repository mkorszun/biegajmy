package com.biegajmy.comments;

import android.support.v4.app.Fragment;
import com.biegajmy.R;
import com.biegajmy.general.ExpandableHeightListView;
import com.biegajmy.model.Comment;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_comments_placeholder) public class CommentsListPlaceholderFragment extends Fragment {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";
    private static final int COMMENTS_LIMIT = 3;

    private String eventID;
    private CommentsListAdapter adapter;
    private ArrayList<Comment> comments;
    @ViewById(R.id.comment_list) ExpandableHeightListView commentList;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        comments = (ArrayList<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);

        adapter = new CommentsListAdapter(getActivity(), getLast(comments));
        commentList.setAdapter(adapter);
        commentList.setExpanded(true);
    }

    @Click(R.id.comment_add) public void onClick() {
        CommentsListActivity_.intent(getActivity())
            .extra(CommentsListActivity.EVENT_ID_ARG, eventID)
            .extra(CommentsListActivity.COMMENTS_ARG, comments)
            .start();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        adapter.clear();
        commentList.setAdapter(null);
        eventID = null;
        adapter = null;
        comments = null;
        commentList = null;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private List<Comment> getLast(List<Comment> comments) {
        int size = comments.size();
        if (size > COMMENTS_LIMIT) {
            int start = comments.size() - COMMENTS_LIMIT;
            return comments.subList(start, comments.size());
        } else {
            return comments;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
