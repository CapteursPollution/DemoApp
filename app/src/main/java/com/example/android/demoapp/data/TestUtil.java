package com.example.android.demoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtil {

    private static List<ContentValues> generateFakeData(){
        //create a list of fake concentration
        List<ContentValues> list = new ArrayList<>();
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

    public static void insertFakeData(Context context) {
        context.getContentResolver().delete(MainContract.ColumnEntries.CONTENT_URI,null,null);
        List<ContentValues> list = TestUtil.generateFakeData();
        Uri uri;
        for(ContentValues cv:list){
            uri = context.getContentResolver().insert(MainContract.ColumnEntries.CONTENT_URI,cv );
            Log.d(context.getClass().getSimpleName(), uri != null ? uri.toString() : null);
        }
    }

}