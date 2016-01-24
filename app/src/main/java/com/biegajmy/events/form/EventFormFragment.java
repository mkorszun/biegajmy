package com.biegajmy.events.form;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventDateTime;
import com.biegajmy.events.EventMapBuilder;
import com.biegajmy.events.EventPace;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.location.LocationActivity;
import com.biegajmy.location.LocationActivity_;
import com.biegajmy.model.Event;
import com.biegajmy.tags.TagEditListFragment;
import com.biegajmy.tags.TagEditListFragment_;
import com.biegajmy.validators.TextFormValidator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_event_form) @OptionsMenu(R.menu.menu_event_new) public abstract class EventFormFragment
    extends ModelFragment<Event> implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private LatLng location;
    protected EventDateTime eventDateTime;
    protected EventPace eventPace;

    @Bean protected TextFormValidator validator;
    @Bean protected LocalStorage storage;
    @Bean protected EventMapBuilder eventMap;

    @ViewById(R.id.form_event_headline) protected TextView headline;
    @ViewById(R.id.form_event_description) protected TextView description;
    @ViewById(R.id.form_event_date) protected TextView date;
    @ViewById(R.id.form_event_time) protected TextView time;
    @ViewById(R.id.form_event_distance) protected TextView distance;
    @ViewById(R.id.form_event_pace) protected TextView pace;

    @ViewById(R.id.form_event_headline_layout) protected TextInputLayout headlineLayout;
    @ViewById(R.id.form_event_description_layout) protected TextInputLayout descriptionLayout;
    @ViewById(R.id.form_event_date_layout) protected TextInputLayout dateLayout;
    @ViewById(R.id.form_event_time_layout) protected TextInputLayout timeLayout;
    @ViewById(R.id.form_event_distance_layout) protected TextInputLayout distanceLayout;
    @ViewById(R.id.form_event_pace_layout) protected TextInputLayout paceLayout;

    @StringRes(R.string.event_form_button_possitive) protected String POSITIVE;
    @StringRes(R.string.event_form_button_negative) protected String NEGATIVE;

    //********************************************************************************************//
    // CALLBACKS
    //********************************************************************************************//

    public abstract void save();

    public abstract LatLng location();

    public abstract void afterViews();

    public abstract ArrayList<String> setTags();

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @AfterViews public void setContent() {
        location = location();
        eventDateTime = new EventDateTime();
        eventPace = new EventPace(0, 0);

        setUpMap(location);
        afterViews();

        Fragment fr = TagEditListFragment_.builder().arg(TagEditListFragment.ARGS_TAGS, setTags()).build();
        getChildFragmentManager().beginTransaction().replace(R.id.tags_container, fr).commit();
    }

    @OptionsItem(R.id.action_event_save) public void createOrUpdateEvent() {
        if (validator.validate1(fields())) save();
    }

    @Click(R.id.form_event_time) public void setTime() {
        EventDateTime.EventTime eventTime = eventDateTime.getTime();
        eventTime.set(time.getText().toString());

        int hour = eventTime.getHour();
        int minute = eventTime.getMinute();

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        timePicker.setButton(DialogInterface.BUTTON_POSITIVE, POSITIVE, timePicker);
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, NEGATIVE, timePicker);
        timePicker.show();
    }

    @Click(R.id.form_event_date) public void setDate() {
        EventDateTime.EventDate eventDate = eventDateTime.getDate();
        eventDate.set(this.date.getText().toString());

        int year = eventDate.getYear();
        int month = eventDate.getMonth() - 1;
        int day = eventDate.getDay();

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.setButton(DialogInterface.BUTTON_POSITIVE, POSITIVE, datePicker);
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, NEGATIVE, datePicker);
        datePicker.show();
    }

    @Click(R.id.form_event_pace) public void setPace() {

        int mm = eventPace.getMinutes();
        int ss = eventPace.getSeconds();

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
            new TimePickerDialog.OnTimeSetListener() {
                @Override public void onTimeSet(TimePicker view, int minutes, int seconds) {
                    eventPace.setMinutes(minutes);
                    eventPace.setSeconds(seconds);
                    pace.setText(eventPace.toString());
                }
            }, mm, ss, true);
        timePicker.setButton(DialogInterface.BUTTON_POSITIVE, POSITIVE, timePicker);
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, NEGATIVE, timePicker);
        timePicker.show();
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

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == LocationActivity.LOCATION_PROVIDED) {
            location = data.getParcelableExtra(LocationActivity.LOCATION_ARG);
            eventMap.updateMarker(location);
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        eventMap.clear();
    }

    public List<String> getTags() {
        return ((TagEditListFragment) getChildFragmentManager().findFragmentById(R.id.tags_container)).getTags();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setUpMap(final LatLng loc) {
        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        ((SupportMapFragment) fr).getMapAsync(new OnMapReadyCallback() {
            @Override public void onMapReady(GoogleMap googleMap) {
                eventMap.setInitialPosition(loc)
                    .setMap(googleMap)
                    .setTitle("")
                    .setOnMarkerClickListener(getMarkerClickListener())
                    .setOnClickListener(getMapClickListener())
                    .build();
            }
        });
    }

    @NonNull private GoogleMap.OnMarkerClickListener getMarkerClickListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override public boolean onMarkerClick(Marker marker) {
                startMapActivity();
                return true;
            }
        };
    }

    @NonNull private GoogleMap.OnMapClickListener getMapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                startMapActivity();
            }
        };
    }

    private void startMapActivity() {
        Intent it = new Intent(getActivity(), LocationActivity_.class);
        it.putExtra(LocationActivity.LOCATION_ARG, location);
        startActivityForResult(it, 111);
    }

    private Map<TextInputLayout, Integer> fields() {
        Map<TextInputLayout, Integer> map = new HashMap<>();
        map.put(headlineLayout, R.string.event_form_headline_error_msg);
        map.put(dateLayout, R.string.event_form_date_error_msg);
        map.put(timeLayout, R.string.event_form_time_error_msg);
        map.put(distanceLayout, R.string.event_form_distance_error_msg);
        return map;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
