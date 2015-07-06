package com.biegajmy.task;

public interface DeleteEventExecutor {

    void onSuccess();

    void onFailure(Exception e);
}
