package com.biegajmy.task;

public interface CreateEventExecutor {

    public void onSuccess();

    public void onFailure(Exception e);
}
