package com.biegajmy.task;

public interface JoinEventExecutor {
    public void onSuccess();

    public void onFailure(Exception e);
}
