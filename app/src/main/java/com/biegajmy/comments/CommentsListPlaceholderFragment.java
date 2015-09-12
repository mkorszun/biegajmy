package com.biegajmy.comments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
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

        adapter = new CommentsListAdapter(getActivity(), CommentsUtils.getLast(comments));
        commentList.setAdapter(adapter);
        commentList.setExpanded(true);
        commentList.setOnItemClickListener(this);
        EventListBus.getInstance().register(this);
    }

    @Click(R.id.comment_add) public void onClick() {
        CommentsListActivity_.intent(getActivity())
            .extra(CommentsListActivity.EVENT_ID_ARG, eventID)
            .extra(CommentsListActivity.COMMENTS_ARG, comments)
            .start();
    }

    @Override public void onDestroy() {
        super.onDestroy();

        commentList.setAdapter(null);
        commentList.setOnItemClickListener(null);
        EventListBus.getInstance().unregister(this);

        adapter.clear();
        eventID = null;
        adapter = null;
        comments = null;
        commentList = null;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommentsListActivity_.intent(getActivity())
            .extra(CommentsListActivity.EVENT_ID_ARG, eventID)
            .extra(CommentsListActivity.COMMENTS_ARG, comments)
            .extra(CommentsListActivity.EDIT_MODE_ARG, false)
            .start();
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(ArrayList<Comment> comments) {
        adapter.clear();
        adapter.addAll(comments);
        adapter.notifyDataSetChanged();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
