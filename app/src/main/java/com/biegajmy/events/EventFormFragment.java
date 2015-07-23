package com.biegajmy.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.validators.TextFormValidator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.Map;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_form) @OptionsMenu(R.menu.menu_event_new)
public abstract class EventFormFragment extends Fragment
    implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    protected EventDateTime eventDateTime;

    @Bean TextFormValidator validator;
    @Bean protected LocalStorage storage;
    @Bean protected EventMapBuilder eventMap;

    @ViewById(R.id.form_event_headline) protected TextView headline;
    @ViewById(R.id.form_event_description) protected TextView description;
    @ViewById(R.id.form_event_date) protected TextView date;
    @ViewById(R.id.form_event_time) protected TextView time;
    @ViewById(R.id.form_event_duration) protected TextView duration;
    @ViewById(R.id.form_event_spots) protected TextView spots;
    @ViewById(R.id.form_event_tags) protected TextView tags;
    @ViewById(R.id.form_event_distance) protected TextView distance;
    @ViewById(R.id.form_event_pace) protected TextView pace;

    //********************************************************************************************//
    // CALLBACKS
    //********************************************************************************************//

    public abstract void save();

    public abstract LatLng location();

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @AfterViews public void setContent() {
        eventDateTime = new EventDateTime();
        if (mMap == null) setUpMap(location());
    }

    @OptionsItem(R.id.action_event_save) public void createOrUpdateEvent() {
        if (validator.validate(fields())) save();
    }

    @Click(R.id.form_event_time) public void setTime() {
        EventDateTime.EventTime eventTime = eventDateTime.getTime();
        eventTime.set(time.getText().toString());

        int hour = eventTime.getHour();
        int minute = eventTime.getMinute();

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        timePicker.show();
    }

    @Click(R.id.form_event_date) public void setDate() {
        EventDateTime.EventDate eventDate = eventDateTime.getDate();
        eventDate.set(this.date.getText().toString());

        int year = eventDate.getYear();
        int month = eventDate.getMonth() - 1;
        int day = eventDate.getDay();

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.show();
    }

    @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EventDateTime.EventTime eventTime = eventDateTime.getTime();
        eventTime.setMinute(minute);
        eventTime.setHour(hourOfDay);
        time.setText(eventTime.toString());
        time.setError(null);
    }

    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EventDateTime.EventDate eventDate = eventDateTime.getDate();
        eventDate.setYear(year);
        eventDate.setMonth(monthOfYear + 1);
        eventDate.setDay(dayOfMonth);
        date.setText(eventDate.toString());
        date.setError(null);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setUpMap(LatLng loc) {

        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.form_event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            eventMap.setDraggable(true).setInitialPosition(loc).setMap(mMap).setTitle("").build();
        }
    }

    private Map<TextView, Integer> fields() {
        Map<TextView, Integer> map = new HashMap<>();
        map.put(headline, R.string.event_form_headline_error_msg);
        map.put(description, R.string.event_form_description_error_msg);
        map.put(date, R.string.event_form_date_error_msg);
        map.put(time, R.string.event_form_time_error_msg);
        map.put(duration, R.string.event_form_duration_error_msg);
        map.put(spots, R.string.event_form_spots_error_msg);
        map.put(tags, R.string.event_form_tags_error_msg);
        map.put(distance, R.string.event_form_distance_error_msg);
        map.put(pace, R.string.event_form_pace_error_msg);
        return map;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
