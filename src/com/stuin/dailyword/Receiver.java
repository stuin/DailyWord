package com.stuin.dailyword;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Stuart on 4/29/2017.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Word word = new Word("04/30 Manualist");
        boolean lock = intent.getBooleanExtra("Lock",false);

        show(word, lock, context);
    }

    public static void show(Word word, Boolean lock, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(word.id).setContentText(MainActivity.format.format(word.date));
        builder.setOngoing(lock);

        Uri url = Uri.parse("http://merriam-webster.com/dictionary/" + word.id);
        Intent siteIntent = new Intent(Intent.ACTION_VIEW, url);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(siteIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(12, builder.build());
    }
}
