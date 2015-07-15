package com.biegajmy.tags;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.biegajmy.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.tag_edit_view) public class TagEditListFragment extends Fragment {

    @ViewById(R.id.new_tag) EditText newTag;

    @AfterViews public void setUp() {
        newTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                TagListBus.getInstance().post(v.getText().toString());
                v.setText("");
                return false;
            }
        });
    }
}
