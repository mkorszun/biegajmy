package com.biegajmy.comments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListBus;
import com.biegajmy.model.Comment;
import com.biegajmy.utils.SystemUtils;
import com.squareup.otto.Subscribe;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_comments) public class CommentsListFragment extends Fragment
    implements TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";
    public static final String EDIT_MODE_ARG = "EDIT_MODE_ARG";
    public static final String TEXT_EMPTY = "";

    private String eventID;
    private boolean editMode;
    private CommentsListAdapter adapter;

    @Bean protected LocalStorage localStorage;
    @ViewById(R.id.comment_add) protected EditText commentAdd;
    @ViewById(R.id.comment_list) protected ListView commentList;
    @ViewById(R.id.swipe_layout) protected SwipeRefreshLayout swipeRefreshLayout;
    @StringRes(R.string.comment_add_failed_msg) protected String ERROR;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListBus.getInstance().register(this);
        List<Comment> comments = (List<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);
        editMode = getArguments().getBoolean(EDIT_MODE_ARG);
        adapter = new CommentsListAdapter(getActivity(), comments);
    }

    @AfterViews public void setup() {
        commentList.setAdapter(adapter);
        commentAdd.setOnEditorActionListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(SystemUtils.progressBarColors());
        if (editMode) SystemUtils.requestFocus(getActivity(), commentAdd);
    }

    @Click(R.id.event_main_comment_add_button) public void add() {
        addComment(commentAdd.getText().toString());
        commentAdd.setText(TEXT_EMPTY);
    }

    @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        addComment(v.getText().toString());
        v.setText(TEXT_EMPTY);
        return true;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().post(adapter.getComments());
        EventListBus.getInstance().unregister(this);
        adapter.clear();
        commentList.setAdapter(null);
        commentAdd.setOnEditorActionListener(null);
        swipeRefreshLayout.setOnRefreshListener(null);
    }

    @Override public void onRefresh() {
        EventBackendService_.intent(getActivity()).getComments(eventID).start();
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventAddCommentOK event) {
        adapter.addAll(event.comments);
    }

    @Subscribe public void event(EventListBus.EventAddCommentNOK event) {
        Toast.makeText(getActivity(), R.string.comment_add_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(EventListBus.GetCommentsOK event) {
        adapter.addAll(event.comments);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe public void event(EventListBus.GetCommentsNOK event) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), R.string.comment_add_failed_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void addComment(String comment) {
        EventBackendService_.intent(getActivity()).addComment(eventID, comment).start();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
