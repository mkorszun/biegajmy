package com.biegajmy.events;

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
            EventListBus.getInstance().post(new EventListBus.EventCreateNOK());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
