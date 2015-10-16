package com.biegajmy.general;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.biegajmy.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity @OptionsMenu(R.menu.menu_event_share) public abstract class FacebookShareActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private String URL = "";
    private String title = "";
    private String description = "";

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OptionsItem(R.id.action_share_event) public void share() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(title)
                .setContentDescription(description)
                .setContentUrl(Uri.parse(URL))
                .build();
            shareDialog.show(linkContent);
        }
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
