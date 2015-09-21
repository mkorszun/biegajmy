package com.biegajmy.events.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import com.biegajmy.events.form.create.EventNewActivity_;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EFragment(R.layout.fragment_user_event_list_main) @OptionsMenu(R.menu.menu_add) public class EventUserListMainFragment
    extends Fragment {

    @OptionsItem(R.id.action_add_event) public void newEvent() {
        startActivity(new Intent(getActivity(), EventNewActivity_.class));
    }
}
