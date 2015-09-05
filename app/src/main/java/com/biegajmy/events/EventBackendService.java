package com.biegajmy.events;

import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.NewEvent;
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
            backend.updateEvent(id, event, localStorage.getToken().token);
            EventListBus.getInstance().post(new EventListBus.EventUpdateOK());
        } catch (Exception e) {
            Log.e(TAG, "Event update failed", e);
            EventListBus.getInstance().post(new EventListBus.EventUpdateNOK());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
