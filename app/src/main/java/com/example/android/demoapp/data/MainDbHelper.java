package com.example.android.demoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nrutemby on 09/04/2018.
 */

public class MainDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "concentration_pm.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + MainContract.ColumnEntries.TABLE_NAME + " (" +
            MainContract.ColumnEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MainContract.ColumnEntries.PM25 + " FLOAT NOT NULL, " +
            MainContract.ColumnEntries.PM10 + " FLOAT NOT NULL, " +
            MainContract.ColumnEntries.DATETIME + " DATETIME NOT NULL" +
            "); ";

    public MainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MainContract.ColumnEntries.TABLE_NAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }
}
