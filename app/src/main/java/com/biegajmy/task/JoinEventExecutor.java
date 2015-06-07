package com.biegajmy.task;

import com.biegajmy.model.Event;

public interface JoinEventExecutor {
    public void onSuccess(Event e);

    public void onFailure(Exception e);
}
