package com.bonepeople.android.future.future.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bonepeople.android.future.future.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplicationContext(), ADActivity.class));

        setContentView(R.layout.activity_main);

        Button _button_list = findViewById(R.id.button_list);
        _button_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_list:
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
                break;
        }
    }
}
