package com.biegajmy.events;

import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;
import java.util.List;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;

@EIntentService public class EventBackendService extends AbstractIntentService {

    private static final String TAG = EventBackendService.class.getName();

    @Bean LocalStorage localStorage;

    public EventBackendService() {
        super(TAG);
    }

    //********************************************************************************************//
    // Actions
    //********************************************************************************************//

    @ServiceAction public void createEvent(NewEvent event) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            backend.createEvent(localStorage.getToken().token, event);
            EventListBus.getInstance().post(new EventListBus.EventCreateOK());
        } catch (Exception e) {
            Log.e(TAG, "Event creation failed", e);
            EventListBus.getInstance().post(new EventListBus.EventCreateNOK());
        }
    }

    @ServiceAction public void updateEvent(String id, NewEvent event) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            Event updated = backend.updateEvent(id, event, localStorage.getToken().token);
            EventListBus.getInstance().post(new EventListBus.EventUpdateOK(updated));
        } catch (Exception e) {
            Log.e(TAG, "Event update failed", e);
            EventListBus.getInstance().post(new EventListBus.EventUpdateNOK());
        }
    }

    @ServiceAction public void listUserEvents() {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            List<Event> events = backend.listEvents(localStorage.getToken().id, localStorage.getToken().token);
            EventListBus.getInstance().post(new EventListBus.ListUserEventsOK(events));
        } catch (Exception e) {
            Log.e(TAG, "List user event failed", e);
            EventListBus.getInstance().post(new EventListBus.ListUserEventsNOK());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
