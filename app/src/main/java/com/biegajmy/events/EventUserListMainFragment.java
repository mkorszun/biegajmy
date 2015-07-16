package com.biegajmy.events;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_user_event_list_main) public class EventUserListMainFragment
    extends Fragment {

    @Click(R.id.new_event) public void newEvent() {
        startActivity(new Intent(getActivity(), EventNewActivity_.class));
    }
}
