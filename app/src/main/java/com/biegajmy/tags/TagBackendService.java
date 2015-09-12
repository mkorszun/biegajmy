package com.biegajmy.tags;

import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.location.LastLocation;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;

@EIntentService public class TagBackendService extends AbstractIntentService {

    private static final String TAG = TagBackendService.class.getName();
    private static final int MAX = 5000;

    @Bean LocalStorage localStorage;
    private BackendInterface backend = BackendInterfaceFactory.build();

    public TagBackendService() {
        super(TAG);
    }

    //********************************************************************************************//
    // Actions
    //********************************************************************************************//

    @ServiceAction public void getTagRecommendations() {
        try {
            List<String> tags = backend.getTagRecommendations();
            localStorage.updateTagRecommendations(new ArrayList(tags));
            Log.d(TAG, "Synced tag recommendations: " + tags.size());
        } catch (Exception e) {
            Log.e(TAG, "Failed to get tag recommendations", e);
        }
    }

    @ServiceAction public void getPopularTags() {
        try {
            LastLocation loc = localStorage.getLastLocation();
            List<String> tags = backend.getPopularTags(loc.lat, loc.lng, MAX);
            localStorage.updatePopularTags(new ArrayList(tags));
            Log.d(TAG, "Synced popular tags: " + tags.size());
        } catch (Exception e) {
            Log.e(TAG, "Failed to get popular tags", e);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
