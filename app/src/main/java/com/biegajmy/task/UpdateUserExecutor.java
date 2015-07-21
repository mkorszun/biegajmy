package com.biegajmy.task;

public interface UpdateUserExecutor {

    public void onSuccess(String id);

    public void onFailure(Exception e);
}
