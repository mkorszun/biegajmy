package com.biegajmy.task;

import com.biegajmy.model.Event;

public interface UpdateEventExecutor {

    void onSuccess(Event event);

    void onFailure(Exception e);
}
