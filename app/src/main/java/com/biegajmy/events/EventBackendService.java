package com.biegajmy.events;

import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.gcm.UserMessageBus;
import com.biegajmy.model.Comment;
import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;
import java.util.ArrayList;
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
            localStorage.updateCacheBreaker();
            EventListBus.getInstance().post(new EventListBus.EventCreateOK());
        } catch (Exception e) {
            Log.e(TAG, "Event creation failed", e);
            EventListBus.getInstance().post(new EventListBus.EventCreateNOK());
        }
    }

    @ServiceAction public void getEvent(String id) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            Event event = backend.getEvent(id);
            localStorage.put(id, event);
            EventListBus.getInstance().post(new EventListBus.GetEventDetailsOK(event));
        } catch (Exception e) {
            Log.e(TAG, "Get event details failed", e);
            EventListBus.getInstance().post(new EventListBus.GetEventDetailsNOK());
        }
    }

    @ServiceAction public void updateEvent(String id, NewEvent event) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            Event updated = backend.updateEvent(id, event, localStorage.getToken().token);
            localStorage.put(id, updated);
            localStorage.updateCacheBreaker();
            EventListBus.getInstance().post(new EventListBus.EventUpdateOK(updated));
        } catch (Exception e) {
            Log.e(TAG, "Event update failed", e);
            EventListBus.getInstance().post(new EventListBus.EventUpdateNOK());
        }
    }

    @ServiceAction public void deleteEvent(String id) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            backend.deleteEvent(id, localStorage.getToken().token);
            localStorage.updateCacheBreaker();
            EventListBus.getInstance().post(new EventListBus.DeleteEventOK(null));
        } catch (Exception e) {
            Log.e(TAG, "Event delete failed", e);
            EventListBus.getInstance().post(new EventListBus.DeleteEventNOK(e));
        }
    }

    @ServiceAction public void listUserEvents() {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            List<Event> events = backend.listEvents(localStorage.getToken().id, localStorage.getToken().token);
            localStorage.put("USER_EVENTS", new ArrayList(events));
            EventListBus.getInstance().post(new EventListBus.ListUserEventsOK(events));
            UserMessageBus.getInstance().post(new UserMessageBus.CheckMessages());
        } catch (Exception e) {
            Log.e(TAG, "List user events failed", e);
            EventListBus.getInstance().post(new EventListBus.ListUserEventsNOK());
        }
    }

    @ServiceAction public void searchEvents(double x, double y, int max, String tags) {
        try {
            List<Event> events;
            BackendInterface backend = BackendInterfaceFactory.build();

            if (tags != null && !tags.isEmpty()) {
                events = backend.listEvents(x, y, max, tags, localStorage.getCacheBreaker());
            } else {
                events = backend.listEvents(x, y, max, localStorage.getCacheBreaker());
            }

            EventListBus.getInstance().post(new EventListBus.SearchEventsOK(events));
        } catch (Exception e) {
            Log.e(TAG, "Search events failed", e);
            EventListBus.getInstance().post(new EventListBus.SearchEventsNOK(e));
        }
    }

    @ServiceAction public void joinEvent(String eventID, boolean join) {
        try {
            Event event;
            BackendInterface backend = BackendInterfaceFactory.build();

            if (join) {
                event = backend.joinEvent(eventID, localStorage.getToken().token);
            } else {
                event = backend.leaveEvent(eventID, localStorage.getToken().token);
            }

            localStorage.put(eventID, event);
            localStorage.updateCacheBreaker();
            EventListBus.getInstance().post(new EventListBus.EventJoinLeaveOK(event));
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to %s event", join ? "join" : "leave"), e);
            EventListBus.getInstance().post(new EventListBus.EventJoinLeaveNOK(e));
        }
    }

    @ServiceAction public void addComment(String eventID, String msg) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            List<Comment> comments = backend.comment(eventID, msg, localStorage.getToken().token).comments;
            localStorage.updateCacheBreaker();
            EventListBus.getInstance().post(new EventListBus.EventAddCommentOK(comments));
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to add comment to event %s", eventID), e);
            EventListBus.getInstance().post(new EventListBus.EventAddCommentNOK(e));
        }
    }

    @ServiceAction public void getComments(String eventID) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            List<Comment> comments = backend.getComments(eventID).comments;
            EventListBus.getInstance().post(new EventListBus.GetCommentsOK(comments));
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to get comments for event %s", eventID));
            EventListBus.getInstance().post(new EventListBus.GetCommentsNOK());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
