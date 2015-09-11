package com.biegajmy.comments;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListBus;
import com.biegajmy.model.Comment;
import com.squareup.otto.Subscribe;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_comments) public class CommentsListFragment extends Fragment
    implements TextView.OnEditorActionListener {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";
    public static final String EDIT_MODE_ARG = "EDIT_MODE_ARG";
    public static final String TEXT_EMPTY = "";

    private String eventID;
    private CommentsListAdapter adapter;

    @Bean protected LocalStorage localStorage;
    @ViewById(R.id.comment_add) protected EditText commentAdd;
    @ViewById(R.id.comment_list) protected ListView commentList;
    @StringRes(R.string.comment_add_failed_msg) protected String ERROR;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        EventListBus.getInstance().register(this);
        List<Comment> comments = (List<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);
        boolean editMode = getArguments().getBoolean(EDIT_MODE_ARG);

        adapter = new CommentsListAdapter(getActivity(), comments);
        commentList.setAdapter(adapter);
        commentAdd.setOnEditorActionListener(this);
        if (editMode) requestFocus();
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
        EventListBus.getInstance().unregister(this);
        adapter.clear();
        commentList.setAdapter(null);
        commentAdd.setOnEditorActionListener(null);
        eventID = null;
        adapter = null;
        commentAdd = null;
        commentList = null;
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventAddCommentOK event) {
        adapter.clear();
        adapter.addAll(event.comments);
        adapter.notifyDataSetChanged();
    }

    @Subscribe public void event(EventListBus.EventAddCommentNOK event) {
        Toast.makeText(getActivity(), R.string.comment_add_failed_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void requestFocus() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        commentAdd.requestFocus();
    }

    private void addComment(String comment) {
        EventBackendService_.intent(getActivity()).addComment(eventID, comment).start();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
