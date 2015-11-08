package com.biegajmy.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.biegajmy.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.StringRes;

@EBean(scope = EBean.Scope.Singleton) public class NotificationSender {

    @RootContext Context context;
    @SystemService NotificationManager notificationManager;
    @StringRes(R.string.push_message_title) String TITLE;

    public void send(String msg) {
        //Intent intent = new Intent(context, MainChallengeActivity_.class);
        //PendingIntent pIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new Notification.Builder(context).setContentTitle(TITLE)
            .setContentText(msg)
            .setSmallIcon(R.drawable.oval_icon)
            .setAutoCancel(true)
            .setStyle(new Notification.BigTextStyle().bigText(msg))
            .build();

        notificationManager.notify(msg.hashCode(), n);
    }
}
