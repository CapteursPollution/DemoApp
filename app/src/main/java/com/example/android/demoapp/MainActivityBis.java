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

import android.content.Intent;
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

import com.example.android.demoapp.network.JSONUtils;
import com.example.android.demoapp.network.NetworkUtils;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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

        ChartUtils.initChart(mChart);

        if (savedInstanceState != null) {
            String SearchUrlString = savedInstanceState.getString(SEARCH_URL_EXTRA);
            mUrlDisplayTextView.setText(SearchUrlString);
        }

        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);
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
    public void onLoadFinished(Loader<String> loader, String dataJsonStr) {
        String data = JSONUtils.getSimpleDataStringFromJson(dataJsonStr);
        ArrayList<Float[]> pmDataList = JSONUtils.getPMDataFromJson(dataJsonStr);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if ("" == data) {
            showErrorMessage();
        } else {
            mSearchResultsTextView.setText(data);
            for (Float[] pmData: pmDataList) {ChartUtils.addEntry(mChart,pmData);}
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
        Boolean bool;
        switch (item.getItemId()) {
            case R.id.action_search:
                makeSearchQuery();
                bool = true;
                break;
            case R.id.storage:
                Intent intent = new Intent(MainActivityBis.this, MainActivity.class);
                startActivity(intent);
                bool = true;
                break;
            default:
                bool = super.onOptionsItemSelected(item);
                break;

        }
        return bool;
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