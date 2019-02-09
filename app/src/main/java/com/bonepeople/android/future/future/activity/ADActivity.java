package com.bonepeople.android.future.future.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bonepeople.android.future.future.R;
import com.r0adkll.slidr.Slidr;

public class ADActivity extends AppCompatActivity {
    private TextView textView_timer;
    CountDownTimer timer;
    private int tickCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        Slidr.attach(this);

        textView_timer = findViewById(R.id.textView_timer);

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView_timer.setText(getString(R.string.caption_text_timer, tickCount));
                tickCount--;
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
