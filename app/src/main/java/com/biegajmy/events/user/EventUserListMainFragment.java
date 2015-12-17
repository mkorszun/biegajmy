package com.biegajmy.events.user;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.biegajmy.R;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_event_list_main) public class EventUserListMainFragment extends Fragment {
    @ViewById(R.id.empty_list_label) protected TextView emptyLabel;

    public void onEmpty(boolean empty) {
        emptyLabel.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
