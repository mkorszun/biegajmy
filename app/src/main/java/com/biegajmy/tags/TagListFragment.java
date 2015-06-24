package com.biegajmy.tags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apmem.tools.layouts.FlowLayout;

@EFragment(R.layout.tag_view) public class TagListFragment extends Fragment {

    private static final String ARGS_TAGS = TagListFragment.class.getName();
    private static final int[] COLORS = { 0xffaa66cc, 0xff669900 };

    private ArrayList<String> tags;
    @ViewById(R.id.tag_root_view) FlowLayout rootView;

    public static TagListFragment_ newInstance(ArrayList<String> tags) {
        Bundle bundle = new Bundle();
        TagListFragment_ fr = new TagListFragment_();

        bundle.putSerializable(ARGS_TAGS, tags);
        fr.setArguments(bundle);

        return fr;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags = (ArrayList<String>) getArguments().getSerializable(ARGS_TAGS);
    }

    @AfterViews public void setUpTags() {
        for (int i = 0; i < tags.size(); i++) {
            rootView.addView(getTag(i));
        }
    }

    private TagListElement getTag(int i) {
        return new TagListElement(getActivity(), tags.get(i), COLORS[i % COLORS.length]);
    }
}
