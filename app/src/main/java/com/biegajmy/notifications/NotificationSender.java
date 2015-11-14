package com.biegajmy.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity_;
import com.biegajmy.events.details.EventDetailActivity_;
import com.biegajmy.events.details.EventDetailFragment;
import com.biegajmy.model.Event;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.StringRes;

@EBean(scope = EBean.Scope.Singleton) public class NotificationSender {

    @RootContext Context context;
    @SystemService NotificationManager notificationManager;
    @StringRes(R.string.push_message_title) String TITLE;

    public void send(String msg, String event_id, String headline) {
        int id = msg.hashCode();

        Intent intent = new Intent(context, EventMainActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intent2 = new Intent(context, EventDetailActivity_.class);
        intent2.putExtra(EventDetailFragment.ARG_EVENT, Event.build(headline, "", event_id));

        Intent[] intents = { intent, intent2 };
        PendingIntent pIntent = PendingIntent.getActivities(context, id, intents, PendingIntent.FLAG_ONE_SHOT);

        Notification n = new Notification.Builder(context).setContentTitle(TITLE)
            .setContentText(msg)
            .setContentIntent(pIntent)
            .setSmallIcon(R.drawable.oval_icon)
            .setAutoCancel(true)
            .setStyle(new Notification.BigTextStyle().bigText(msg))
            .build();

        notificationManager.notify(id, n);
    }
}
