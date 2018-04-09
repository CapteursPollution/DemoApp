package com.example.android.demoapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.demoapp.data.MainContract;
import com.example.android.demoapp.data.MainDbHelper;
import com.example.android.demoapp.data.TestUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycler;

    private MainAdapter mAdapter;

    private Cursor mCursor;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycler = (RecyclerView) findViewById(R.id.rv);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);

        MainDbHelper dbHelper = new MainDbHelper(this);
        db = dbHelper.getWritableDatabase();

        TestUtil.insertFakeData(db);
        mCursor = getAllData();

        mAdapter = new MainAdapter(mCursor);
        mRecycler.setAdapter(mAdapter);

        mRecycler.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_refresh == item.getItemId()) {
            mAdapter = new MainAdapter(mCursor);
            mRecycler.setAdapter(mAdapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Cursor getAllData() {
        return db.query(MainContract.ColumnEntries.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MainContract.ColumnEntries.DATETIME);
    }
}
