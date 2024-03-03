package com.example.tears_dont_fall;

import android.app.Application;

import com.example.tears_dont_fall.Model.MySP;
import com.example.tears_dont_fall.Activities.SignalGenerator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.init(this);
        DataManager.init();
        SignalGenerator.init(this);
    }
}
