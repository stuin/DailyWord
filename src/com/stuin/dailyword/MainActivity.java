package com.stuin.dailyword;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import com.stuin.cleanvisuals.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private Words words;

    static DateFormat format = new SimpleDateFormat("MM/dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = new Words(this);
        display();
        if(words.wordList.size() < 2) words.next();
    }

    @Override
    protected void onPause() {
        super.onPause();
        words.save();
    }

    public void display() {
        //Make list of words
        StringBuilder stringBuilder = new StringBuilder();
        for(Word w : words.wordList) {
            String out = w.print() + "\n";
            stringBuilder.append(out);
        }

        ((TextView) findViewById(R.id.Word)).setText(stringBuilder.toString());
    }

    public void start(View view) {
        //Get word
        Word word = words.get();
        if(view.getId() == R.id.Word) words.next();

        //Get lock
        boolean lock = ((Switch) findViewById(R.id.Lock)).isChecked();

        //Set Notification
        //show(word, 10);
        Receiver.show(word, lock, this);
    }

    public void clear(View view) {
        words.wordList.clear();
        words.next();
        words.next();
    }

    private void show(Word word, int delay) {
        //Prepare Notification
        Intent notificationIntent = new Intent(this, Receiver.class);
        notificationIntent.putExtra("Word", word.print());
        notificationIntent.putExtra("Lock", ((Switch) findViewById(R.id.Lock)).isChecked());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 12, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
