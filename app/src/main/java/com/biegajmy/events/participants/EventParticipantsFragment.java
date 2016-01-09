package com.biegajmy.events.participants;

import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.model.User;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apmem.tools.layouts.FlowLayout;

@EFragment(R.layout.fragment_event_participants) public class EventParticipantsFragment
    extends ModelFragment<ArrayList<User>> {

    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static final String ARG_PARTICIPANTS = "ARG_PARTICIPANTS";

    @ViewById(R.id.event_participants) protected FlowLayout flowLayout;
    @ViewById(R.id.comment_label_count) protected TextView count;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return ARG_PARTICIPANTS;
    }

    @AfterViews public void setup() {
        update(model);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void update(ArrayList<User> participants) {
        flowLayout.removeAllViews();

        int res1 = R.string.event_participants_count_1;
        int res2 = R.string.event_participants_count_2;
        int size = participants.size();
        int res = size == 1 ? res1 : res2;
        count.setText(getString(res, size));
        int count = size > 20 ? 20 : size;

        for (User u : participants.subList(0, count)) {
            flowLayout.addView(new EventParticipantsLayout(getActivity(), u.photo_url));
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
