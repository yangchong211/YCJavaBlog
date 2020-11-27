package com.yc.checktool.help;

import android.app.Application;

public class CheckAppTool {


    private static CheckAppTool INSTANCE;
    private Application app;

    /**
     * 获取NetworkTool实例 ,单例模式
     */
    public static CheckAppTool getInstance() {
        if (INSTANCE == null) {
            synchronized (CheckAppTool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CheckAppTool();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Application application){
        this.app = application;
    }

    public Application getApplication() {
        return app;
    }



}
