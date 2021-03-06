package com.biegajmy.events.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventListBus;
import com.biegajmy.tags.TagListFragment;
import com.biegajmy.tags.TagListFragment_;
import com.biegajmy.utils.SystemUtils;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_search_settings) public class EventSearchSettingsFragment extends Fragment
    implements TextView.OnEditorActionListener, View.OnClickListener {

    private int lastRange = 10000;
    private Bus bus = EventListBus.getInstance();

    @ViewById(R.id.dist1) protected Button dist1;
    @ViewById(R.id.dist2) protected Button dist2;
    @ViewById(R.id.dist3) protected Button dist3;
    @ViewById(R.id.dist4) protected Button dist4;
    @ViewById(R.id.dist5) protected Button dist5;
    @ViewById(R.id.dist6) protected Button dist6;
    @ViewById(R.id.tag_edit_text) protected AutoCompleteTextView addTag;
    @ViewById(R.id.tag_add_confirmation) protected ImageButton confirmationButton;

    @Bean LocalStorage localStorage;

    private boolean clearMode;
    private String previousTag = "";

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        addTag.setAdapter(null);
        addTag.setOnEditorActionListener(null);
        addTag.addTextChangedListener(null);
    }

    @AfterViews @UiThread public void setup() {
        dist3.setSelected(true);

        List<String> popular = localStorage.getPopularTags();
        List<String> recommendations = localStorage.getTagRecommendations();

        TagListFragment fr = TagListFragment_.builder()
            .arg(TagListFragment.ARGS_TAGS, new ArrayList(popular))
            .arg(TagListFragment.ARGS_HIDE_LABEL, true)
            .build();

        addTag.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, recommendations));
        addTag.setOnEditorActionListener(this);

        fr.setOnClickListener(this);
        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().add(R.id.popular_tags, fr).commitAllowingStateLoss();
    }

    @Click(R.id.dist1) public void one_km() {
        setSelected(dist1);
        bus.post(new EventSearchRange(lastRange = 1000, addTag.getText().toString()));
    }

    @Click(R.id.dist2) public void three_km() {
        setSelected(dist2);
        bus.post(new EventSearchRange(lastRange = 5000, addTag.getText().toString()));
    }

    @Click(R.id.dist3) public void five_km() {
        setSelected(dist3);
        bus.post(new EventSearchRange(lastRange = 10000, addTag.getText().toString()));
    }

    @Click(R.id.dist4) public void ten_km() {
        setSelected(dist4);
        bus.post(new EventSearchRange(lastRange = 25000, addTag.getText().toString()));
    }

    @Click(R.id.dist5) public void twenty_five_km() {
        setSelected(dist5);
        bus.post(new EventSearchRange(lastRange = 50000, addTag.getText().toString()));
    }

    @Click(R.id.dist6) public void fifty_km() {
        setSelected(dist6);
        bus.post(new EventSearchRange(lastRange = 3000000, addTag.getText().toString()));
    }

    @Click(R.id.tag_add_confirmation) public void searchTag() {
        actionForTag();
    }

    @Click(R.id.tag_edit_text) public void editTag() {
        setButton(clearMode = false);
    }

    @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        actionForTag();
        return false;
    }

    @Override public void onClick(View v) {
        TextView tag = (TextView) v.findViewById(R.id.tag_view);
        addTag.setText(tag.getText().toString());
        clearMode = false;
        actionForTag();
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void setClearMode() {
        addTag.setText(previousTag);
        addTag.dismissDropDown();
        addTag.clearFocus();
        setButton(clearMode = !previousTag.isEmpty());
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setSelected(Button button) {
        dist1.setSelected(false);
        dist2.setSelected(false);
        dist3.setSelected(false);
        dist4.setSelected(false);
        dist5.setSelected(false);
        dist6.setSelected(false);
        button.setSelected(true);
    }

    private void actionForTag() {
        SystemUtils.hideKeyboard(getActivity());
        if (addTag.getText().length() == 0 && !clearMode) return;
        if (clearMode) addTag.setText(null);

        addTag.setSelection(addTag.getText().length());
        addTag.dismissDropDown();
        addTag.clearFocus();
        setButton(clearMode = !clearMode);
        bus.post(new EventSearchRange(lastRange, previousTag = addTag.getText().toString()));
    }

    private void setButton(boolean clearMode) {
        int res = clearMode ? R.drawable.ic_clear_white_18dp : R.drawable.ic_done_white_24dp;
        confirmationButton.setImageResource(res);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
