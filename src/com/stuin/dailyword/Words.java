package com.stuin.dailyword;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.stuin.cleanvisuals.Request;
import org.xml.sax.DTDHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Stuart on 3/30/2017.
 */
public class Words {
    List<Word> wordList = new ArrayList<>();
    Date date;

    private boolean full = false;
    private SharedPreferences sharedPreferences;
    private MainActivity mainActivity;

    Words(MainActivity mainActivity) {
        date = new Date();
        this.mainActivity = mainActivity;

        sharedPreferences = mainActivity.getSharedPreferences("DailyWord", Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet("Words", new HashSet<>());

        for(String s : set) wordList.add(new Word(s));
    }

    void next() {
        Request.address = "randomword.setgetgo.com";
        word.start("get.php");
    }

    Word get() {
        while(wordList.size() > 1 && value(wordList.get(0).date) != value(date)) {
            wordList.remove(wordList.get(0));
        }
        return wordList.get(0);
    }

    void save() {
        Set<String> set = new HashSet<>();
        for(Word w : wordList) set.add(w.print());

        sharedPreferences.edit().putStringSet("Words", set).apply();
    }

    private int value(Date date) {
        DateFormat format = new SimpleDateFormat("MMdd", Locale.US);
        return Integer.valueOf(format.format(date));
    }

    private Request word = new Request() {
        @Override
        public void run(List<String> s) {
            super.run(s);
            String out = s.get(0);
            out = out.substring(0, 1).toUpperCase() + out.substring(1);

            if(full) {
                Request.address = "oed-api-demo-2445581300291.apicast.io:443/oed/api/v0.0/words";
                details.start("?lemma=" + out);
            } else {
                Word word = new Word().set(wordList.size());
                word.lemma = out;
                wordList.add(word);
            }
        }
    };

    private Request details = new Request() {
        @Override
        public void run(List<String> s) {
            super.run(s);

            StringBuilder stringBuilder = new StringBuilder();
            for(String string : s) stringBuilder.append(string);

            Gson gson = new Gson();
            wordList.add(gson.fromJson(stringBuilder.toString(), Word.class).set(wordList.size()));
        }
    };
}
