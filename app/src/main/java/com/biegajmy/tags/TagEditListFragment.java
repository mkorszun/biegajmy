package com.biegajmy.tags;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.utils.SystemUtils;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

@EFragment(R.layout.tag_edit_view) public class TagEditListFragment extends Fragment
    implements MaterialDialog.InputCallback, DialogInterface.OnDismissListener {

    public static final String ARGS_TAGS = "ARGS_TAGS";

    @Bean protected LocalStorage localStorage;
    @FragmentById(R.id.tag_list_container) protected TagListFragment tagListFragment;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> tags = (ArrayList<String>) getArguments().getSerializable(ARGS_TAGS);

        Fragment fr = TagListFragment_.builder()
            .arg(TagListFragment.ARGS_TAGS, tags)
            .arg(TagListFragment.ARGS_EDITABLE, true)
            .build();

        getChildFragmentManager().beginTransaction().add(R.id.tag_list_container, fr).commit();
    }

    @Click(R.id.new_tag) public void newTag() {
        new MaterialDialog.Builder(getActivity()).inputType(InputType.TYPE_CLASS_TEXT)
            .positiveText(R.string.tag_add)
            .input(null, null, false, this)
            .dismissListener(this)
            .show();
    }

    @Override public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
        getTagListFragment().addTag(charSequence.toString());
    }

    @Override public void onDismiss(DialogInterface dialog) {
        SystemUtils.hideKeyboard(getActivity());
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public List<String> getTags() {
        return getTagListFragment().getTags();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private TagListFragment getTagListFragment() {
        return tagListFragment == null ? tagListFragment = getFragmentById() : tagListFragment;
    }

    private TagListFragment getFragmentById() {
        return (TagListFragment) getChildFragmentManager().findFragmentById(R.id.tag_list_container);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
