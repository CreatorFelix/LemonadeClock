package com.creator.lemonade.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.creator.lemonade.sample.R;
import com.creator.lemonade.sample.adapter.DemoListAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDemoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadDemoList();
    }

    private void initView() {
        recyclerViewDemoList = findViewById(R.id.recyclerView_demo_list);
    }

    private void loadDemoList() {
        recyclerViewDemoList.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewDemoList.setAdapter(new DemoListAdapter(this));
    }
}
