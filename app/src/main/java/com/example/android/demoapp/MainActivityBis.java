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
package com.example.android.demoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import com.example.android.demoapp.network.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivityBis extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private static final String SEARCH_URL_EXTRA = "query";

    private static final int SEARCH_LOADER_ID = 22;

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_bis);
        mSearchBoxEditText = findViewById(R.id.et_search_box);
        mUrlDisplayTextView = findViewById(R.id.tv_url_display);
        mSearchResultsTextView = findViewById(R.id.tv_search_results_json);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mChart = findViewById(R.id.lineChart);

        initChart(mChart);

        if (savedInstanceState != null) {
            String SearchUrlString = savedInstanceState.getString(SEARCH_URL_EXTRA);
            mUrlDisplayTextView.setText(SearchUrlString);
        }

        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);
    }

    private void initChart(LineChart mChart) {

        // customize line chart
        Description des = new Description();
        des.setText("");
        mChart.setDescription(des);

        // enable value highlighting
        // mChart.setDefaultFocusHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling & dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // enable pinch zoom to avoid scaling x and y axis separately
        mChart.setPinchZoom(true);

        // alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        // DATA
        LineData data = new LineData();
        data.setValueTextColor(Color.RED);

        // add data to line chart
        mChart.setData(data);

        // get legend object
        Legend l = mChart.getLegend();

        // customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);


        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(true);
        x1.setAvoidFirstLastClipping(true);
        x1.setEnabled(false);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        // auto set y axis min max with 5% spacing:
        float percent = 5;
        y1.setSpaceTop(percent);
        y1.setSpaceBottom(percent);
        y1.setDrawGridLines(true);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);

    }

    private void addEntry(Float a, Float b) {
        LineData data = mChart.getData();

        if (data != null) {

            // APhsA
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet("APhsA");
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), a), 0);

            // APhsB
            set = data.getDataSetByIndex(1);

            if (set == null) {
                set = createSet("APhsB");
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), b), 1);

            // let the chart know it's data has changed
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
//            mChart.setVisibleXRangeMaximum(15);
            float minXRange = 10;
            float maxXRange = 10;
            mChart.setVisibleXRange(minXRange, maxXRange);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet(String label) {
        LineDataSet set = new LineDataSet(null, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        Integer color;
        switch (label){
            case "APhsA":
                color = Color.RED;
                break;
            case "APhsB":
                color = Color.GREEN;
                break;
            default:
                color = Color.BLACK;
        }
        set.setColors(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setValueTextColor(color);
        set.setValueTextSize(10f);
        // To show values of each point
        set.setDrawValues(true);

        return set;
    }

    private void makeSearchQuery() {

        String filter = mSearchBoxEditText.getText().toString().replace("%2C", ",");

        if (TextUtils.isEmpty(filter)) {
            mUrlDisplayTextView.setText("No query entered, nothing to search for.");
            return;
        }

        URL searchUrl = NetworkUtils.buildUrl(filter);
        mUrlDisplayTextView.setText(searchUrl.toString().replace("%2C",","));

        Bundle searchBundle = new Bundle();
        searchBundle.putString(SEARCH_URL_EXTRA, searchUrl.toString().replace("%2C",","));

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> searchLoader = loaderManager.getLoader(SEARCH_LOADER_ID);
        if (searchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER_ID, searchBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER_ID, searchBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                forceLoad();
            }

            @Override
            public String loadInBackground() {


                String searchUrlString = args.getString(SEARCH_URL_EXTRA);

                if (searchUrlString == null || TextUtils.isEmpty(searchUrlString)) {
                    return null;
                }

                try {
                    URL searchUrl = new URL(searchUrlString);
                    String searchResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    return searchResult;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (null == data) {
            showErrorMessage();
        } else {
            mSearchResultsTextView.setText(data);
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_bis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            makeSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String searchUrlString = mUrlDisplayTextView.getText().toString().replace("%2C",",");
        outState.putString(SEARCH_URL_EXTRA, searchUrlString);
    }

    private void showJsonDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}