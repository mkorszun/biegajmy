package com.biegajmy.general;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.biegajmy.R;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.res.StringRes;

@EActivity @OptionsMenu(R.menu.menu_event_share) public abstract class ShareActivity extends AppCompatActivity {

    private String URL = "";
    private String title = "";
    private String description = "";

    @StringRes(R.string.share) protected String SHARE;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @OptionsItem(R.id.action_share_event) public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, title);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, description);
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, SHARE));
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
