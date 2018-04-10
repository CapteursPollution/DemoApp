package com.example.android.demoapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.demoapp.data.MainContract;
import com.example.android.demoapp.data.MainDbHelper;
import com.example.android.demoapp.data.TestUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecycler;

    private MainAdapter mAdapter;

    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycler = (RecyclerView) findViewById(R.id.rv);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);

        getContentResolver().delete(MainContract.ColumnEntries.CONTENT_URI,null,null);
        insertFakeData();
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

    public void insertFakeData() {
        List<ContentValues> list = TestUtil.generateFakeData();
        Uri uri;
        for(ContentValues cv:list){
         uri = getContentResolver().insert(MainContract.ColumnEntries.CONTENT_URI,cv );
         Log.d(TAG,uri.toString());
        }
    }

    public Cursor getAllData() {
        try {
            return getContentResolver().query(MainContract.ColumnEntries.CONTENT_URI,
                    null,
                    null,
                    null,
                    MainContract.ColumnEntries._ID);

        } catch(Exception e) {
            Log.e(TAG, "Failed to load data");
            e.printStackTrace();
            return null;
        }
    }
}
