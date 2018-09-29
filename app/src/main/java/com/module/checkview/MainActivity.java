package com.module.checkview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.module.checkview.data.Data;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_executor;
    ExecutorAdapter adapter;
    Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data.initData();
        initView();
        initLayoutMananger();
        initAdapter();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        rv_executor = findViewById(R.id.rv_executor);
        setSupportActionBar(toolbar);
    }

    private void initLayoutMananger(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_executor.setLayoutManager(linearLayoutManager);
    }

    private void initAdapter() {
        adapter = new ExecutorAdapter(this, data.groupBeans);
        rv_executor.setAdapter(adapter);
    }
}
