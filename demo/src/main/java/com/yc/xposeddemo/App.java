package com.yc.xposeddemo;

import android.app.Application;

import com.yc.checktool.help.CheckAppTool;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CheckAppTool.getInstance().init(this);
    }


}
