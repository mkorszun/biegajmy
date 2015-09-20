package com.biegajmy.events.search;

public class EventSearchRange {
    private int max;
    private String tags;

    public EventSearchRange(int max, String tags) {
        this.max = max;
        this.tags = tags;
    }

    public int getMax() {
        return max;
    }

    public String getTags() {
        return tags;
    }
}
