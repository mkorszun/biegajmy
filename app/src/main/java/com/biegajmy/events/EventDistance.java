package com.biegajmy.events;

import com.biegajmy.utils.StringUtils;

public class EventDistance {

    private Double distance;

    public EventDistance(double distance) {
        this.distance = distance;
    }

    @Override public String toString() {
        String dist = StringUtils.doubleToString(distance);
        return dist != null ? String.format("%s km", dist) : null;
    }

    public String toString(String emptyPlaceholder) {
        String str = toString();
        return str != null ? str : emptyPlaceholder;
    }
}
