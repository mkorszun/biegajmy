package com.biegajmy.tags;

import com.squareup.otto.Bus;
import java.util.ArrayList;

public class TagListBus extends Bus {

    private static TagListBus bus;

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new TagListBus();
        }

        return bus;
    }

    public static class NewTagEvent {
        public String tag;

        public NewTagEvent(String tag) {
            this.tag = tag;
        }
    }

    public static class SaveTagsEvent {}
}
