package com.biegajmy.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.User;
import com.biegajmy.tags.TagListBus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

public class UserBackendService extends Service {

    private LocalStorage localStorage;

    @Override public void onCreate() {
        super.onCreate();
        localStorage = new LocalStorage(getApplicationContext());
        TagListBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        TagListBus.getInstance().unregister(this);
    }

    @Override public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe public void updateTags(TagListBus.UpdateTagsEvent event) {
        User user = localStorage.getUser();
        user.setTags(event.tags);
        localStorage.updateUser(user);
    }
}
