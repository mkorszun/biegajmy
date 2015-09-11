package com.biegajmy.tags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.tag_edit_view) public class TagEditListFragment extends Fragment
    implements TextView.OnEditorActionListener {

    public static final String ARGS_TAGS = "ARGS_TAGS";

    @Bean protected LocalStorage localStorage;
    @ViewById(R.id.new_tag) protected EditText newTag;

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

    @Override public void onDestroy() {
        super.onDestroy();
        newTag.setOnEditorActionListener(null);
    }

    @AfterViews public void setUp() {
        newTag.setOnEditorActionListener(this);
    }

    @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        getTagListFragment().addTag(v.getText().toString());
        v.setText("");
        return false;
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
        return (TagListFragment) getChildFragmentManager().findFragmentById(R.id.tag_list_container);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
