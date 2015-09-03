package com.biegajmy.events;

import com.biegajmy.model.Event;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override public int compare(Event lhs, Event rhs) {
        return Long.valueOf(lhs.timestamp).compareTo(Long.valueOf(rhs.timestamp));
    }
}