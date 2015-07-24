package com.biegajmy.task;

public interface TaskExecutor<T> {

    void onSuccess(T t);

    void onFailure(Exception e);
}
