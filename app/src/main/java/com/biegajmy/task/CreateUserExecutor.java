package com.biegajmy.task;

public interface CreateUserExecutor {

    public void onSuccess(String id);

    public void onFailure(Exception e);
}
