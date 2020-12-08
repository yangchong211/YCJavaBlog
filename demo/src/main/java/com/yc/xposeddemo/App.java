package com.yc.xposeddemo;

import android.app.Application;
import android.util.Log;

import com.yc.checktool.help.CheckAppTool;
import com.yc.checktool.utils.ToolLogUtils;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToolLogUtils.d("初始化-----App---");
    }


}
