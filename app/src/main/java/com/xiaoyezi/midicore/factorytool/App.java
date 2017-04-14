package com.xiaoyezi.midicore.factorytool;

import android.app.Application;

import com.xiaoyezi.midicore.factorytool.utils.Tlog;

/**
 * Created by jim on 2017/4/13.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Tlog.initLog();
    }
}
