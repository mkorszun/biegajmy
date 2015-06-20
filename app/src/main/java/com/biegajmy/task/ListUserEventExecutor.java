package com.biegajmy.task;

import com.biegajmy.model.Event;
import java.util.List;

public interface ListUserEventExecutor {

    void onSuccess(List<Event> events);

    void onFailure(Exception e);
}
