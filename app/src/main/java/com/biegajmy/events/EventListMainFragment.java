package com.biegajmy.events;

import android.support.v4.app.Fragment;
import android.widget.SeekBar;
import com.biegajmy.R;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_list_main) public class EventListMainFragment extends Fragment
    implements SeekBar.OnSeekBarChangeListener {

    private Bus bus = EventListBus.getInstance();
    @ViewById(R.id.seekBar) protected SeekBar seekBar;

    @AfterViews public void setContent() {
        seekBar.setOnSeekBarChangeListener(this);
        bus.post(new EventRange(seekBar.getProgress()));
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
        bus.post(new EventRange(seekBar.getProgress()));
    }
}
