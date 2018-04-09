package com.example.android.demoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nrutemby on 09/04/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.DataViewHolder> {

    private String TAG = MainAdapter.class.getSimpleName();

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
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DataViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_item_textView);
        }

        public void bind() {
            textView.setText("Test");
        }
    }
}
