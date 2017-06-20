package com.woowstudio.firebasetest;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pipiks on 20/06/2017.
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
