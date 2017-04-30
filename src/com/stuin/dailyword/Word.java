package com.stuin.dailyword;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Stuart on 3/30/2017.
 */
public class Word {
    String id;
    String definition;
    String[] parts_of_speech;

    Date date = new Date();

    Word() {
    }

    Word(String input) {
        id = input.split(" ")[1];
        try {
            date = MainActivity.format.parse(input.split(" ")[0]);
        } catch(ParseException e) {
            //Do nothing
        }
    }

    Word set(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        this.date = cal.getTime();

        return this;
    }

    String print() {
        return  MainActivity.format.format(date) + ' ' + id;
    }
}
