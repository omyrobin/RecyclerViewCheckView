package com.module.checkview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.module.checkview.data.Data;

public class MainActivity extends AppCompatActivity implements ExecutorAdapter.IallSelectStatusListener{

    Toolbar toolbar;
    RecyclerView rv_executor;
    ExecutorAdapter adapter;
    ImageButton ib_all_check;
    Data data = new Data();
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data.initData();
        initView();
        initLayoutMananger();
        setAllCheck();
        initAdapter();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        rv_executor = findViewById(R.id.rv_executor);
        ib_all_check = findViewById(R.id.ib_all_check);
        setSupportActionBar(toolbar);
    }

    private void initLayoutMananger(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_executor.setLayoutManager(linearLayoutManager);
    }

    private void setAllCheck(){
        ib_all_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = status == Constants.CHECK ? Constants.UNCHECK : Constants.CHECK;
                if(adapter!=null){
                    adapter.putMap(status);
                    adapter.setImageButtonStatus(status, ib_all_check);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initAdapter() {
        adapter = new ExecutorAdapter(this, data.groupBeans, this);
        rv_executor.setAdapter(adapter);
    }

    @Override
    public void setAllSelectStatus(int status) {
        this.status = status;
        if(adapter!=null){
            adapter.setImageButtonStatus(status, ib_all_check);
        }
    }
}
