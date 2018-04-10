package com.example.android.demoapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nrutemby on 09/04/2018.
 */

public class MainContract {

    public static final String AUTHORITY = "com.example.android.demoapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_ALL = "all";

    public static final class ColumnEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ALL).build();

        public static final String TABLE_NAME = "concentration_pm";

        public static final String PM25 =  "pm25";
        public static final String PM10 =  "pm10";
        public static final String DATETIME = "datetime";
    }
}
