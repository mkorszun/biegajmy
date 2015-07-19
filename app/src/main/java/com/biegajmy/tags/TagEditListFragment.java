package com.biegajmy.tags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.backend.UserBackendService;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.tag_edit_view) @OptionsMenu(R.menu.menu_edit_tags)
public class TagEditListFragment extends Fragment {

    @Bean LocalStorage localStorage;
    @ViewById(R.id.new_tag) EditText newTag;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> tags = new ArrayList<>(localStorage.getUser().tags);

        Fragment fr = TagListFragment_.builder()
            .arg(TagListFragment.ARGS_TAGS, tags)
            .arg(TagListFragment.ARGS_EDITABLE, true)
            .build();

        getChildFragmentManager().beginTransaction().add(R.id.tag_list_container, fr).commit();
        getActivity().startService(new Intent(getActivity(), UserBackendService.class));
    }

    @Override public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), UserBackendService.class));
    }

    @AfterViews public void setUp() {
        newTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                TagListBus.getInstance().post(new TagListBus.NewTagEvent(v.getText().toString()));
                v.setText("");
                return false;
            }
        });
    }

    @OptionsItem(R.id.action_tags_update) public void updateTags() {
        TagListBus.getInstance().post(new TagListBus.SaveTagsEvent());
    }
}
