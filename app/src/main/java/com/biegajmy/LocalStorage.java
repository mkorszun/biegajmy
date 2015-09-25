package com.biegajmy;

import android.content.Context;
import com.biegajmy.location.LastLocation;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import io.paperdb.Paper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LocalStorage {

    private static final String TAG = LocalStorage.class.getName();
    private static final String USER = "user";

    @RootContext Context context;

    @AfterInject void init() {
        Paper.init(context);
    }

    public LocalStorage(Context context) {
        this.context = context;
        this.init();
    }

    public <T> T get(String key, Class<T> clazz) {
        return Paper.book().read(key);
    }

    public void put(String key, Serializable value) {
        Paper.book().write(key, value);
    }

    public User getUser() {
        return this.get("user", User.class);
    }

    public void updateUser(User user) {
        this.put("user", user);
    }

    public Token getToken() {
        return this.get("token", Token.class);
    }

    public void updateToke(Token token) {
        this.put("token", token);
    }

    public boolean hasToken() {
        return getToken() != null;
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
        return Paper.book().exist("user");
    }

    public void updateTagRecommendations(ArrayList<String> tags) {
        this.put("tag_recommendations", tags);
    }

    public List<String> getTagRecommendations() {
        return this.get("tag_recommendations", ArrayList.class);
    }

    public void updatePopularTags(ArrayList<String> tags) {
        this.put("tag_popular", tags);
    }

    public List<String> getPopularTags() {
        return this.get("tag_popular", ArrayList.class);
    }
}
