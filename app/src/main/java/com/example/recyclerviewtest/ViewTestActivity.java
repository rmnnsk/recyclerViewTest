package com.example.recyclerviewtest;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import  android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewTestActivity extends AppCompatActivity {
    int index = 10;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.list);
        final ArrayList<String> dataSet = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            dataSet.add("Item id = " + String.valueOf(i));
        }
        final MyAdapter adapter = new MyAdapter();
        adapter.addData(dataSet);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        final Context activity = getApplicationContext();
        list.addOnItemTouchListener(
                new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String cp = dataSet.get(position);
                        Intent intent = new Intent(ViewTestActivity.this, Activity2.class);
                        intent.putExtra("data", cp);
                        ViewTestActivity.this.startActivity(intent);
                    }
                })
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> newData = new ArrayList<>();
                newData.add("Index id = " + String.valueOf(index));
                index++;
                adapter.addData(newData);
                /*adapter.notifyDataSetChanged();*/
                adapter.notifyItemInserted(index);
            }
        });
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener mListener;
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }
        GestureDetector mGestureDetector;
        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        public String text;
        private final List<String> data;

        public MyAdapter() {
            data = new ArrayList<>();
        }


        public void addData(List<String> inData) {
            data.addAll(inData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return MyViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
            this.text = data.get(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_recycler_item);
        }

        public static MyViewHolder create(ViewGroup parent) {
            return new MyViewHolder( LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item, parent, false));
        }
    }
}
