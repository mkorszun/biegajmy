package com.biegajmy.comments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import com.biegajmy.R;
import com.biegajmy.events.EventListBus;
import com.biegajmy.general.ExpandableHeightListView;
import com.biegajmy.model.Comment;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_comments_placeholder) public class CommentsListPlaceholderFragment extends Fragment
    implements AdapterView.OnItemClickListener {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";
    public static final String COMMENTS_READ_ONLY_ARG = "COMMENTS_READ_ONLY_ARG";

    private String eventID;
    private boolean readOnly;

    private CommentsListAdapter adapter;
    private ArrayList<Comment> comments;

    @ViewById(R.id.comment_add) protected Button addComment;
    @ViewById(R.id.comment_view) protected Button viewComment;
    @ViewById(R.id.comment_list) protected ExpandableHeightListView commentList;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListBus.getInstance().register(this);
        comments = (ArrayList<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        readOnly = getArguments().getBoolean(COMMENTS_READ_ONLY_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);
        adapter = new CommentsListAdapter(getActivity(), CommentsUtils.getLast(comments), R.layout.comment_list_item2);
    }

    @AfterViews public void setup() {
        commentList.setAdapter(adapter);
        commentList.setExpanded(true);
        commentList.setOnItemClickListener(this);
        addComment.setVisibility(readOnly ? View.GONE : View.VISIBLE);

        boolean moreComments = !readOnly && comments.size() > CommentsUtils.COMMENTS_LIMIT;
        viewComment.setVisibility(moreComments ? View.VISIBLE : View.GONE);
    }

    @Click(R.id.comment_add) public void onClick() {
        if (!readOnly) {
            CommentsListActivity_.intent(getActivity())
                .extra(CommentsListActivity.EVENT_ID_ARG, eventID)
                .extra(CommentsListActivity.COMMENTS_ARG, comments)
                .start();
        }
    }

    @Click(R.id.comment_view) public void viewAll() {
        if (!readOnly) viewAllComments();
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!readOnly) viewAllComments();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        commentList.setAdapter(null);
        commentList.setOnItemClickListener(null);
        EventListBus.getInstance().unregister(this);
        adapter.clear();
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void update(ArrayList<Comment> comments) {
        adapter.clear();
        adapter.addAll(CommentsUtils.getLast(comments));
        this.comments.clear();
        this.comments.addAll(comments);

        boolean moreComments = !readOnly && comments.size() > CommentsUtils.COMMENTS_LIMIT;
        viewComment.setVisibility(moreComments ? View.VISIBLE : View.GONE);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void viewAllComments() {
        CommentsListActivity_.intent(getActivity())
            .extra(CommentsListActivity.EVENT_ID_ARG, eventID)
            .extra(CommentsListActivity.COMMENTS_ARG, comments)
            .extra(CommentsListActivity.EDIT_MODE_ARG, false)
            .start();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
