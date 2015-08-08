package com.biegajmy.comments;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.general.ExpandableHeightListView;
import com.biegajmy.model.Comment;
import com.biegajmy.model.Event;
import com.biegajmy.task.CommentEventTask;
import com.biegajmy.task.TaskExecutor;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_comments) public class CommentsListFragment extends Fragment {

    public static final String EVENT_ID_ARG = "EVENT_ID_ARG";
    public static final String COMMENTS_ARG = "COMMENTS_ARG";

    private String eventID;
    private CommentsListAdapter adapter;

    @ViewById(R.id.comment_add) EditText commentAdd;
    @ViewById(R.id.comment_list) ExpandableHeightListView commentList;
    @Bean LocalStorage localStorage;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        List<Comment> comments = (List<Comment>) getArguments().getSerializable(COMMENTS_ARG);
        eventID = getArguments().getString(EVENT_ID_ARG);

        adapter = new CommentsListAdapter(getActivity(), comments);
        commentList.setAdapter(adapter);
        commentList.setExpanded(true);
        commentAdd.setOnEditorActionListener(getActionListener());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        adapter.clear();
        commentList.setAdapter(null);
        commentAdd.setOnEditorActionListener(null);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private TextView.OnEditorActionListener getActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                new CommentEventTask(new TaskExecutor<Event>() {
                    @Override public void onSuccess(Event event) {
                        adapter.clear();
                        adapter.addAll(event.comments);
                        adapter.notifyDataSetChanged();
                    }

                    @Override public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Add comment failed", Toast.LENGTH_LONG).show();
                    }
                }).execute(eventID, localStorage.getToken().token, v.getText().toString());

                v.setText("");
                return false;
            }
        };
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
