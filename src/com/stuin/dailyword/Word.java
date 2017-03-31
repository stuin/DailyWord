package com.stuin.dailyword;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Stuart on 3/30/2017.
 */
public class Word {
    String lemma;
    String definition;
    String[] parts_of_speech;

    Date date;

    Word() {

    }

    Word(String input) {
        lemma = input.split(" ")[0];
    }

    Word set(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, days);
        this.date = cal.getTime();

        return this;
    }

    String print() {
        return  lemma + ' ' + MainActivity.format.format(date);
    }
}
