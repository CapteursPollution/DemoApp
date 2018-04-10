package com.example.android.demoapp.data;

import android.content.ContentValues;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtil {

    public static List<ContentValues> generateFakeData(){
        //create a list of fake concentration
        List<ContentValues> list = new ArrayList<ContentValues>();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MainContract.ColumnEntries.PM25, r.nextInt(100));
            cv.put(MainContract.ColumnEntries.PM10, r.nextInt(100));
            cv.put(MainContract.ColumnEntries.DATETIME, Calendar.getInstance().getTime().toString());
            list.add(cv);
        }
        return list;
    }
}