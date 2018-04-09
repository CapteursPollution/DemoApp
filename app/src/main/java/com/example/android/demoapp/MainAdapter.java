package com.example.android.demoapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.demoapp.data.MainContract;

/**
 * Created by nrutemby on 09/04/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.DataViewHolder> {

    private String TAG = MainAdapter.class.getSimpleName();

    private Cursor mCursor;

    public MainAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        Log.d(TAG, "#"+position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DataViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_item_textView);
        }

        public void bind(int position) {
            mCursor.moveToPosition(position);
            int pm25 = mCursor.getColumnIndex(MainContract.ColumnEntries.PM25);
            int pm10 = mCursor.getColumnIndex(MainContract.ColumnEntries.PM10);
            textView.setText("pm25: " +
                            mCursor.getString(pm25)  +
                            " pm10: " +
                            mCursor.getString(pm10));
        }
    }
}
