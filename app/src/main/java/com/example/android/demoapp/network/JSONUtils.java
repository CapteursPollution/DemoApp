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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class JSONUtils {


    public static final String getSimpleDataStringFromJson(String DataJsonStr) {
        String singleDataString = "";
        try {
            ArrayList<String[]> dataList = getDataStringsFromJson(DataJsonStr);
            for (String[] dataStrings: dataList) {
                singleDataString = singleDataString +
                        dataStrings[0] + " - " +
                        dataStrings[1] + " (pm25) - " +
                        dataStrings[2] + " (pm10)" +
                        "\n\n";
            }
            return singleDataString;
        } catch (JSONException e) {
            e.printStackTrace();
            return singleDataString;
        }
    }

    public static final ArrayList<Float[]> getPMDataFromJson(String DataJsonStr) {
        ArrayList<Float[]> pmDataList = new ArrayList<>();
        try {
            ArrayList<String[]> dataList = getDataStringsFromJson(DataJsonStr);
            for (String[] dataStrings: dataList) {
                Float pm25 = Float.parseFloat(dataStrings[1]);
                Float pm10 = Float.parseFloat(dataStrings[2]);
                pmDataList.add(new Float[]{pm25,pm10});
            }
            return pmDataList;
        } catch (JSONException e) {
            e.printStackTrace();
            return pmDataList;
        }

    }

    private static ArrayList<String[]> getDataStringsFromJson(String DataJsonStr)
            throws JSONException {

        final String CONCENTRATION_PM = "Concentration_pm";

        final String COLUMNS_LIST = "columns";

        final String ROWS_LIST = "records";;

        String date_measure;
        String pm25;
        String pm10;

        JSONObject dataJson;

        if(DataJsonStr != null) {
            dataJson = new JSONObject(DataJsonStr);
        } else {
            dataJson = new JSONObject();
        }

        JSONObject concentration_table = dataJson.getJSONObject(CONCENTRATION_PM);

        JSONArray columnsArray = concentration_table.getJSONArray(COLUMNS_LIST);

        int date_measure_index = getIndex(columnsArray, "date_mesure");
        int pm25_index  = getIndex(columnsArray, "pm2_5");
        int pm10_index = getIndex(columnsArray, "pm10");

        JSONArray rowsArray = concentration_table.getJSONArray(ROWS_LIST);

        ArrayList<String[]> parsedData = new ArrayList<>(rowsArray.length());

        for (int i = 0; i < rowsArray.length(); i++) {

            JSONArray measureArray = rowsArray.getJSONArray(i);
            date_measure = measureArray.getString(date_measure_index);
            pm25 = measureArray.getString(pm25_index);
            pm10 = measureArray.getString(pm10_index);

            parsedData.add(new String[]{date_measure, pm25, pm10});
        }

        return parsedData;
    }

    private static final int getIndex(JSONArray jsonArray, String string) {
        int i = 0;
        int size = jsonArray.length();
        try {
            while (!jsonArray.getString(i).equals(string)) {
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