package com.biegajmy.tags;

import com.squareup.otto.Bus;

public class TagListBus extends Bus {

    private static TagListBus bus;

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new TagListBus();
        }

        return bus;
    }
}
