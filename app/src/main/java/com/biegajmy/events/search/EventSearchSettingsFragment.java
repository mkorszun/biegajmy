package com.biegajmy.events.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_search_settings) public class EventSearchSettingsFragment extends Fragment
    implements TextView.OnEditorActionListener {

    private int lastRange = 5000;
    private Bus bus = EventListBus.getInstance();

    @ViewById(R.id.one_km) protected Button button1;
    @ViewById(R.id.three_km) protected Button button3;
    @ViewById(R.id.five_km) protected Button button5;
    @ViewById(R.id.ten_km) protected Button button10;
    @ViewById(R.id.twenty_five_km) protected Button button25;
    @ViewById(R.id.fifty_km) protected Button button50;
    @ViewById(R.id.tag_edit_text) protected AutoCompleteTextView addTag;

    @Bean LocalStorage localStorage;

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
    }

    @AfterViews public void setup() {
        button5.setSelected(true);

        List<String> popular = localStorage.getPopularTags();
        List<String> recommendations = localStorage.getTagRecommendations();

        TagListFragment fr = TagListFragment_.builder()
            .arg(TagListFragment.ARGS_TAGS, new ArrayList(popular))
            .arg(TagListFragment.ARGS_HIDE_LABEL, true)
            .build();

        addTag.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, recommendations));
        addTag.setOnEditorActionListener(this);

        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().add(R.id.popular_tags, fr).commit();
    }

    @Click(R.id.one_km) public void one_km() {
        setSelected(button1);
        bus.post(new EventSearchRange(lastRange = 1000, addTag.getText().toString()));
    }

    @Click(R.id.three_km) public void three_km() {
        setSelected(button3);
        bus.post(new EventSearchRange(lastRange = 3000, addTag.getText().toString()));
    }

    @Click(R.id.five_km) public void five_km() {
        setSelected(button5);
        bus.post(new EventSearchRange(lastRange = 5000, addTag.getText().toString()));
    }

    @Click(R.id.ten_km) public void ten_km() {
        setSelected(button10);
        bus.post(new EventSearchRange(lastRange = 10000, addTag.getText().toString()));
    }

    @Click(R.id.twenty_five_km) public void twenty_five_km() {
        setSelected(button25);
        bus.post(new EventSearchRange(lastRange = 25000, addTag.getText().toString()));
    }

    @Click(R.id.fifty_km) public void fifty_km() {
        setSelected(button50);
        bus.post(new EventSearchRange(lastRange = 50000, addTag.getText().toString()));
    }

    @Click(R.id.tag_add_confirmation) public void searchTag() {
        bus.post(new EventSearchRange(lastRange, addTag.getText().toString()));
        SystemUtils.hideKeyboard(getActivity());
        addTag.dismissDropDown();
    }

    @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        bus.post(new EventSearchRange(lastRange, addTag.getText().toString()));
        addTag.dismissDropDown();
        return false;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setSelected(Button button) {
        button1.setSelected(false);
        button3.setSelected(false);
        button5.setSelected(false);
        button10.setSelected(false);
        button25.setSelected(false);
        button50.setSelected(false);
        button.setSelected(true);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
