package com.biegajmy;

import android.content.Context;
import com.biegajmy.location.LastLocation;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import io.paperdb.Paper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LocalStorage {

    private static final String BOOK_NAME = "biegaj.my";

    private static final String USER = "user";
    private static final String TOKEN = "token";
    private static final String LAST_LOCATION = "last_location";
    private static final String TAG_RECOMMENDATIONS = "tag_recommendations";
    private static final String TAG_POPULAR = "tag_popular";

    @RootContext Context context;

    @AfterInject void init() {
        Paper.init(context);
    }

    public LocalStorage(Context context) {
        this.context = context;
        this.init();
    }

    public <T> T get(String key, Class<T> clazz) {
        return Paper.book(BOOK_NAME).read(key);
    }

    public void put(String key, Serializable value) {
        Paper.book(BOOK_NAME).write(key, value);
    }

    public boolean has(String key) {
        return Paper.book(BOOK_NAME).exist(key);
    }

    //********************************************************************************************//
    // User
    //********************************************************************************************//

    public User getUser() {
        return this.get(USER, User.class);
    }

    public void updateUser(User user) {
        this.put(USER, user);
    }

    //********************************************************************************************//
    // Token
    //********************************************************************************************//

    public Token getToken() {
        return this.get(TOKEN, Token.class);
    }

    public void updateToke(Token token) {
        this.put(TOKEN, token);
    }

    public boolean hasToken() {
        return has(TOKEN);
    }

    //********************************************************************************************//
    // Last location
    //********************************************************************************************//

    public LastLocation getLastLocation() {
        return this.get(LAST_LOCATION, LastLocation.class);
    }

    public synchronized LastLocation updateLastLocation(double lat, double lng) {
        LastLocation loc = get(LAST_LOCATION, LastLocation.class);
        if (loc == null) {
            loc = new LastLocation(lat, lng);
        } else {
            loc.lat = lat;
            loc.lng = lng;
        }

        loc.lastUpdate = Calendar.getInstance().getTime().getTime();
        this.put(LAST_LOCATION, loc);
        return loc;
    }

    //********************************************************************************************//
    // Tag recommendations
    //********************************************************************************************//

    public void updateTagRecommendations(ArrayList<String> tags) {
        this.put(TAG_RECOMMENDATIONS, tags);
    }

    public List<String> getTagRecommendations() {
        ArrayList arrayList = this.get(TAG_RECOMMENDATIONS, ArrayList.class);
        return arrayList != null ? arrayList : Collections.EMPTY_LIST;
    }

    public void updatePopularTags(ArrayList<String> tags) {
        this.put(TAG_POPULAR, tags);
    }

    public List<String> getPopularTags() {
        ArrayList arrayList = this.get(TAG_POPULAR, ArrayList.class);
        return arrayList != null ? arrayList : Collections.EMPTY_LIST;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
