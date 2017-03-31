package com.stuin.dailyword;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private Words words;
    private Word word;

    public static DateFormat format = new SimpleDateFormat("MM/dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = new Words(this);
        words.next();
    }

    @Override
    protected void onPause() {
        super.onPause();
        words.save();
    }

    public void start(View view) {
        word = words.get();
        words.next();
        show(word);

        StringBuilder stringBuilder = new StringBuilder();
        for(Word w : words.wordList) {
            String out = w.print() + "\n";
            stringBuilder.append(out);
        }

        ((TextView) findViewById(R.id.Word)).setText(stringBuilder.toString());
    }

    private void show(Word word) {
        Notification.Builder builder = new Notification.Builder(this);
        builder = builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(word.lemma).setContentText(format.format(word.date));
        builder.setOngoing(((Switch) findViewById(R.id.Lock)).isChecked());

        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(12, builder.build());
    }
}
