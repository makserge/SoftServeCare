package com.softserveinc.softservecare.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.SoftServeCareApplication;
import com.softserveinc.softservecare.api.FirebaseApi;
import com.softserveinc.softservecare.model.Survey;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final int REDIRECT_DELAY = 3000;
    private static final int DEEP_LINK_OFFSET = 23;

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
            if (FirebaseApi.getInstance().checkLogin()) {
                loadLoginView();
            }
            else {
                if (FirebaseApi.getInstance().isSurveysLoaded()) {
                    if (checkDeepLink()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    delayedRedirectToMainActivity();
                }
            }
        }
    };

    private boolean checkDeepLink() {
        Uri data = getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String id = data.toString().substring(DEEP_LINK_OFFSET);
            if (id != null) {
                try {
                    int surveyId = Integer.parseInt(id);

                    Survey survey = FirebaseApi.getInstance().findSurveyById(String.valueOf(surveyId));
                    if (survey != null) {
                        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                        intent.putExtra(SoftServeCareApplication.SURVEY, survey);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        return true;
                    }
                }
                catch (NumberFormatException e) {
                }
                return false;
            }
        }
        return true;
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
