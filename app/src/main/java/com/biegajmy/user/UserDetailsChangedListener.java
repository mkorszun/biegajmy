package com.biegajmy.user;

import com.biegajmy.model.User;

public interface UserDetailsChangedListener {
    void onChanged(User user);
}
