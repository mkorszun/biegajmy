package com.biegajmy.tags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import com.biegajmy.user.UserEventBus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apmem.tools.layouts.FlowLayout;

@EFragment(R.layout.tag_view) public class TagListFragment extends Fragment {

    public static final String ARGS_TAGS = "ARGS_TAGS";
    public static final String ARGS_EDITABLE = "ARGS_EDITABLE";
    private static final int[] COLORS = { 0xffaa66cc, 0xff669900 };

    private boolean isEditable;
    private ArrayList<String> tags = new ArrayList();
    @ViewById(R.id.tag_root_view) protected FlowLayout rootView;

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialTags();

        if (isEditable) TagListBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if (isEditable) TagListBus.getInstance().unregister(this);
    }

    @AfterViews public void setUpTags() {
        for (int i = 0; i < tags.size(); i++) {
            rootView.addView(getTag(i));
        }
    }

    //********************************************************************************************//
    // Events - only for editable mode
    //********************************************************************************************//

    @Subscribe public void onNewTag(TagListBus.NewTagEvent event) {
        tags.add(event.tag);
        rootView.addView(newTag(event.tag));
    }

    @Subscribe public void onSaveTags(TagListBus.SaveTagsEvent event) {
        UserEventBus.getInstance().post(new UserEventBus.UpdateUserTagsEvent(tags));
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private TagListElement getTag(int i) {
        return new TagListElement(getActivity(), tags.get(i), COLORS[i % COLORS.length]);
    }

    private TagListElement newTag(String tag) {
        return new TagListElement(getActivity(), tag, COLORS[(tags.size() - 1) % COLORS.length]);
    }

    private void setInitialTags() {
        Bundle args = getArguments();
        if (args != null) {
            tags.addAll((ArrayList<String>) args.getSerializable(ARGS_TAGS));
            isEditable = args.getBoolean(ARGS_EDITABLE);
        }
    }
    //********************************************************************************************//
    //********************************************************************************************//
}
