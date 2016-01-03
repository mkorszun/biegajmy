package com.biegajmy.tags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.biegajmy.R;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apmem.tools.layouts.FlowLayout;

@EFragment(R.layout.tag_view) public class TagListFragment extends Fragment implements View.OnClickListener {

    public static final String ARGS_TAGS = "ARGS_TAGS";
    public static final String ARGS_HIDE_LABEL = "ARGS_HIDE_LABEL";
    public static final String ARGS_EDITABLE = "ARGS_EDITABLE";
    public static final String ARGS_TAG_RES_ID = "ARGS_TAG_RES_ID";

    private int tagResId = -1;
    private boolean isEditable;
    private boolean hideLabel;
    private View.OnClickListener onClickListener;
    private ArrayList<String> tags = new ArrayList();

    @ViewById(R.id.tag_label) protected TextView tagLabel;
    @ViewById(R.id.tag_root_view) protected FlowLayout rootView;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags.addAll((ArrayList<String>) getArguments().getSerializable(ARGS_TAGS));
        tagResId = getArguments().getInt(ARGS_TAG_RES_ID, R.layout.tag_view_element);
        isEditable = getArguments().getBoolean(ARGS_EDITABLE);
        hideLabel = getArguments().getBoolean(ARGS_HIDE_LABEL);
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @AfterViews public void setUpTags() {
        tagLabel.setVisibility(isEditable || hideLabel ? View.GONE : View.VISIBLE);
        for (int i = 0; i < tags.size(); i++) rootView.addView(getTag(i));
    }

    @Override public void onClick(View v) {
        TextView tw = (TextView) v.findViewById(R.id.tag_view);
        rootView.removeView(v);
        tags.remove(tw.getText());
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public ArrayList<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
        rootView.addView(newTag(tag));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private TagListElement getTag(int i) {
        return new TagListElement(getActivity(), tags.get(i), getListener(), isEditable, tagResId);
    }

    private TagListElement newTag(String tag) {
        return new TagListElement(getActivity(), tag, getListener(), isEditable, tagResId);
    }

    private View.OnClickListener getListener() {
        return isEditable ? this : onClickListener;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
