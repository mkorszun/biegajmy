package com.biegajmy;

import android.content.Context;
import android.util.Log;
import com.biegajmy.location.LastLocation;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.io.Serializable;
import java.util.Calendar;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LocalStorage {

    private static final String TAG = LocalStorage.class.getName();
    private static final String USER = "user";

    @RootContext Context context;
    private DB localStorage;

    @AfterInject void init() {
        try {
            localStorage = DBFactory.open(context);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public LocalStorage(Context context) {
        this.context = context;
        this.init();
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            return localStorage.getObject(key, clazz);
        } catch (SnappydbException e) {
            Log.e(TAG, "Failed to get object", e);
            return null;
        }
    }

    public void put(String key, Serializable value) {
        try {
            localStorage.put(key, value);
        } catch (SnappydbException e) {
            Log.e(TAG, "Failed to put object", e);
        }
    }

    public User getUser() {
        return this.get("user", User.class);
    }

    public void updateUser(User user) {
        this.put("user", user);
    }

    //public String getToken() {
    //    return this.get("token", String.class);
    //}
    //
    //public void updateToken(String token) {
    //    this.put("token", token);
    //}

    public Token getToken2() {
        return this.get("token2", Token.class);
    }

    public void updateToke2(Token token) {
        this.put("token2", token);
    }

    public boolean hasToken2() {
        return getToken2() != null;
    }

    public LastLocation getLastLocation() {
        return this.get("last_location", LastLocation.class);
    }

    public synchronized LastLocation updateLastLocation(double lat, double lng) {
        LastLocation loc = get("last_location", LastLocation.class);
        if (loc == null) {
            loc = new LastLocation(lat, lng);
        } else {
            loc.lat = lat;
            loc.lng = lng;
        }

        loc.lastUpdate = Calendar.getInstance().getTime().getTime();
        this.put("last_location", loc);
        return loc;
    }

    public boolean hasUser() {
        try {
            return this.localStorage.exists("user");
        } catch (SnappydbException e) {
            return false;
        }
    }
}
