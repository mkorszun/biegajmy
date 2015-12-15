package com.biegajmy.user;

import com.biegajmy.model.User;

public interface UserDetailsChangeListener {
    void onChange(User user);

    void onUpdate(User user);
}
