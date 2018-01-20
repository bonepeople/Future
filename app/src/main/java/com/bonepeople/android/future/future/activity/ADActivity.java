package com.bonepeople.android.future.future.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bonepeople.android.future.future.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.r0adkll.slidr.Slidr;

public class ADActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        Slidr.attach(this);

        ShimmerFrameLayout _layout = findViewById(R.id.shimmer);
        _layout.setDuration(2000);
        _layout.startShimmerAnimation();
    }

    @Override
    public void onBackPressed() {

    }
}
