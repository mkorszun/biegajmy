package com.biegajmy.comments;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.Comment;
import com.biegajmy.model.Event;
import com.biegajmy.task.CommentEventTask;
import com.biegajmy.task.TaskExecutor;
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
        List<Comment> comments = (List<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);

        adapter = new CommentsListAdapter(getActivity(), comments);
        commentList.setAdapter(adapter);
        commentAdd.setOnEditorActionListener(this);
        commentAdd.requestFocus();
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
        adapter.clear();
        commentList.setAdapter(null);
        commentAdd.setOnEditorActionListener(null);
        eventID = null;
        adapter = null;
        commentAdd = null;
        commentList = null;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void addComment(String comment) {
        new CommentEventTask(new TaskExecutor<Event>() {
            @Override public void onSuccess(Event event) {
                adapter.clear();
                adapter.addAll(event.comments);
                adapter.notifyDataSetChanged();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(getActivity(), ERROR, Toast.LENGTH_LONG).show();
            }
        }).execute(eventID, localStorage.getToken().token, comment);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
