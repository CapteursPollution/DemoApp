/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Modifications copyright (C) 2018 Nil Rutembya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.demoapp.network;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.demoapp.data.MainContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class JSONUtils {


    public static String[] getDataStringsFromJson(String DataJsonStr)
            throws JSONException {

        final String CONCENTRATION_PM = "Concentration_pm";

        final String COLUMNS_LIST = "columns";

        final String ROWS_LIST = "records";;

        String date_measure;
        String pm25;
        String pm10;

        JSONObject dataJson = new JSONObject(DataJsonStr);

        JSONObject concentration_table = dataJson.getJSONObject(CONCENTRATION_PM);

        JSONArray columnsArray = concentration_table.getJSONArray(COLUMNS_LIST);

        int date_measure_index = getIndex(columnsArray, "date_mesure");
        int pm25_index  = getIndex(columnsArray, "pm2_5");
        int pm10_index = getIndex(columnsArray, "pm10");

        JSONArray rowsArray = concentration_table.getJSONArray(ROWS_LIST);

        String[] parsedData = new String[rowsArray.length()];

        for (int i = 0; i < parsedData.length; i++) {

            JSONArray measureArray = rowsArray.getJSONArray(i);
            System.out.println(measureArray);

            date_measure = measureArray.getString(date_measure_index);
            pm25 = measureArray.getString(pm25_index);
            pm10 = measureArray.getString(pm10_index);

            parsedData[i] = date_measure + " - " + pm25 + " (pm25) - " + pm10 + " (pm10)";
        }

        return parsedData;
    }

    public static String getSingleDataStringFromJson(String DataJsonStr) {
        String singleDataString = "";
        try {
            String[] dataStrings = getDataStringsFromJson(DataJsonStr);
            for (String s : dataStrings) {
                singleDataString = singleDataString + s + "\n\n";
            }
            return singleDataString;
        } catch (JSONException e) {
            e.printStackTrace();
            return singleDataString;
        }
    }

    private static final int getIndex(JSONArray jsonArray, String string) {
        int i = 0;
        int size = jsonArray.length();
        try {
            while (!jsonArray.getString(i).equals(string)) {
                System.out.println("Test");
                if ( i == size-1) {
                     return -2;
                } else {
                    i = i + 1;
                }
            }
            return i;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
}