package com.alien.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alien.captchainputview.CaptchaInputView;
import com.alien.captchainputview.ICaptchaViewListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CaptchaInputView)findViewById(R.id.mCaptchaInputView)).setICaptchaViewListener(new ICaptchaViewListener() {
            @Override
            public void onContentChanged(String content) {
            }

            @Override
            public void onComplete(String content) {
            }
        });
    }
}
