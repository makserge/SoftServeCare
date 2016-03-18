package com.softserveinc.softservecare.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.api.FirebaseApi;

public class SplashActivity extends AppCompatActivity {
    private static final int REDIRECT_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        FirebaseApi.getInstance().initFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();

        delayedRedirectToMainActivity();
    }

    private void delayedRedirectToMainActivity() {
        redirectHandler.removeCallbacks(redirectRunnable);
        redirectHandler.postDelayed(redirectRunnable, REDIRECT_DELAY);
    }

    private final Handler redirectHandler = new Handler();
    private final Runnable redirectRunnable = new Runnable() {
        @Override
        public void run() {
            if (FirebaseApi.getInstance().isSurveysLoaded()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            else {
                delayedRedirectToMainActivity();
            }
        }
    };
}
