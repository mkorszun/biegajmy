package com.biegajmy.events.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import com.biegajmy.R;
import com.biegajmy.events.EventListBus;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_search_settings) public class EventSearchSettingsFragment extends Fragment {

    private Bus bus = EventListBus.getInstance();

    @ViewById(R.id.one_km) protected Button button1;
    @ViewById(R.id.three_km) protected Button button3;
    @ViewById(R.id.five_km) protected Button button5;
    @ViewById(R.id.ten_km) protected Button button10;
    @ViewById(R.id.twenty_five_km) protected Button button25;
    @ViewById(R.id.fifty_km) protected Button button50;

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
    }

    @AfterViews public void setup() {
        button5.setSelected(true);
    }

    @Click(R.id.one_km) public void one_km() {
        setSelected(button1);
        bus.post(new EventSearchRange(1000));
    }

    @Click(R.id.three_km) public void three_km() {
        setSelected(button3);
        bus.post(new EventSearchRange(3000));
    }

    @Click(R.id.five_km) public void five_km() {
        setSelected(button5);
        bus.post(new EventSearchRange(5000));
    }

    @Click(R.id.ten_km) public void ten_km() {
        setSelected(button10);
        bus.post(new EventSearchRange(10000));
    }

    @Click(R.id.twenty_five_km) public void twenty_five_km() {
        setSelected(button25);
        bus.post(new EventSearchRange(25000));
    }

    @Click(R.id.fifty_km) public void fifty_km() {
        setSelected(button50);
        bus.post(new EventSearchRange(50000));
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
