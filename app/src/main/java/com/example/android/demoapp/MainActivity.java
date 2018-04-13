package com.example.android.demoapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.demoapp.data.MainContract;
import com.example.android.demoapp.data.TestUtil;


public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecycler;

    private int mPosition = RecyclerView.NO_POSITION;

    private MainAdapter mAdapter;

    private static final int ID_CURSOR_LOADER = 42;

    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycler = findViewById(R.id.rv);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this);
        mRecycler.setAdapter(mAdapter);

        mRecycler.setHasFixedSize(true);

        showLoading();

        getSupportLoaderManager().initLoader(ID_CURSOR_LOADER,null,this);
    }



    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.getProgressDrawable();
        switch (id) {
            case ID_CURSOR_LOADER:
                TestUtil.insertFakeData(this);
                return new android.support.v4.content.CursorLoader(this,
                        MainContract.ColumnEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        MainContract.ColumnEntries._ID);
            default:
                throw new RuntimeException(TAG + "Loader Not implemented: " + id);
        }
   }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mRecycler.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showDataView();
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean bool;
        switch (item.getItemId()) {
            case R.id.action_refresh:
                LoaderManager loaderManager = getSupportLoaderManager();
                Loader<Cursor> cursorLoader = loaderManager.getLoader(ID_CURSOR_LOADER);
                if (cursorLoader == null) {
                    loaderManager.initLoader(ID_CURSOR_LOADER, null, this);
                } else {
                    loaderManager.restartLoader(ID_CURSOR_LOADER, null, this);
                }
                bool = true;
                break;
            case R.id.chart_menu_item:
                Intent intent = new Intent(MainActivity.this, MainActivityBis.class);
                startActivity(intent);
                bool = true;
                break;
            default:
                bool = super.onOptionsItemSelected(item);
                break;
        }
        return bool;
    }

    private void showLoading() {
        mRecycler.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mRecycler.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }



}
