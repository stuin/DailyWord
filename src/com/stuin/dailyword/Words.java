package com.stuin.dailyword;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stuin.cleanvisuals.EndPoint;
import com.stuin.cleanvisuals.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Stuart on 3/30/2017.
 */
class Words {
    List<Word> wordList = new ArrayList<>();

    private boolean full = false;
    private MainActivity mainActivity;
    private SharedPreferences sharedPreferences;
    private EndPoint[] endPoints;

    Words(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        //Prepare apis
        endPoints = new EndPoint[2];
        endPoints[0] = new EndPoint("setgetgo.com/randomword/");
        endPoints[1] = new EndPoint("od-api.oxforddictionaries.com:443/api/v1");
        endPoints[1].addProp("Accept", "application/json");

        //Get save data
        sharedPreferences = mainActivity.getSharedPreferences("DailyWord", Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet("Words", new HashSet<>());
        while(!set.isEmpty()) {
            String next = "22";
            for(String s : set) if(s.compareTo(next) < 0) next = s;
            set.remove(next);
            wordList.add(new Word(next));
        }

    }

    void next() {
        //Add word
        Request.endPoint = endPoints[0];
        word.start("get.php");
        String s = mainActivity.getResources().getString(R.string.main_load);
        ((TextView) mainActivity.findViewById(R.id.Word)).setText(s);
    }

    Word get() {
        int i = 0;
        int date = value(new Date());

        while(i < wordList.size() && value(wordList.get(i).date) < date) {
            i++;
        }

        if(i == wordList.size()) return new Word();
        return wordList.get(i);
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
            //Get word
            String out = s.get(0);
            out = out.substring(0, 1).toUpperCase() + out.substring(1);

            if(full) {
                Request.endPoint = endPoints[1];
                details.start("entries/en/" + out);
            } else {
                //Add base word
                Word word = new Word().set(wordList.size());
                word.id = out;

                wordList.add(word);
                mainActivity.display();
            }
        }
    };

    private Request details = new Request() {
        @Override
        public void run(List<String> s) {
            super.run(s);

            StringBuilder stringBuilder = new StringBuilder();
            for(String string : s) stringBuilder.append(string);

            JsonParser jsonParser = new JsonParser();
            JsonObject results = jsonParser.parse(stringBuilder.toString())
                    .getAsJsonObject().getAsJsonArray("results").get(0)
                    .getAsJsonObject();
            JsonObject sense = results.getAsJsonArray("lexicalEntries").get(0)
                    .getAsJsonObject().getAsJsonArray("entries").get(0)
                    .getAsJsonObject().getAsJsonArray("senses").get(0)
                    .getAsJsonObject();

            Word word = new Word();
            word.id = results.get("id").getAsString();
            word.definition = sense.getAsJsonArray("definitions").get(0).getAsString();
        }
    };
}
