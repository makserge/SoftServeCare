package com.softserveinc.softservecare;

import android.app.Application;
import com.firebase.client.Firebase;
import com.softserveinc.softservecare.api.FirebaseApi;

public class SoftServeCareApplication extends Application {
    public static final String SURVEY = "SURVEY";

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
        FirebaseApi.init();
    }


}