package com.example.android.demoapp.data;

import android.provider.BaseColumns;

/**
 * Created by nrutemby on 09/04/2018.
 */

public class MainContract {

    public static final class ColumnEntries implements BaseColumns {
        public static final String TABLE_NAME = "concentration_pm";
        public static final String PM25 =  "pm25";
        public static final String PM10 =  "pm10";
        public static final String DATETIME = "datetime";
    }
}
