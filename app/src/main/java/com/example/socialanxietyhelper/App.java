package com.example.socialanxietyhelper;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeManager.init(this);
    }
}