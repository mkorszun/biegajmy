package com.biegajmy.task;

import com.biegajmy.model.Event;
import java.util.List;

public interface ListEventExecutor {

    public void onSuccess(List<Event> events);

    public void onFailure(Exception e);

}
