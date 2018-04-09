package com.example.android.demoapp.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MainContract.ColumnEntries.PM25, r.nextInt(100));
            cv.put(MainContract.ColumnEntries.PM10, r.nextInt(100));
            cv.put(MainContract.ColumnEntries.DATETIME, Calendar.getInstance().getTime().toString());
            list.add(cv);
        }

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (MainContract.ColumnEntries.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MainContract.ColumnEntries.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }
}