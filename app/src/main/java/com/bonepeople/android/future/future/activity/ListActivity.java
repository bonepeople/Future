package com.bonepeople.android.future.future.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bonepeople.android.future.future.R;

public class ListActivity extends AppCompatActivity {
    RecyclerView _recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        _recycler = findViewById(R.id.recyclerView);
    }
}
