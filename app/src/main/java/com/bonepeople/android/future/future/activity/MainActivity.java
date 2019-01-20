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
        getApplication().startActivity(new Intent(this, ADActivity.class));

        setContentView(R.layout.activity_main);

        Button _button_list = findViewById(R.id.button_list);
        _button_list.setOnClickListener(this);
        findViewById(R.id.button_ripple).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_list:
                startActivity(new Intent(this, ListActivity.class));
                break;
            case R.id.button_ripple:
                startActivity(new Intent(this, RippleActivity.class));
                break;
        }
    }
}
