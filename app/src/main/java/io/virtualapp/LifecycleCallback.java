package io.virtualapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2017/03/22
 *     desc  : 生命周期管理类
 *     revise: 老项目中baseActivity，baseFragment未统一，暂时用这个，低侵入性添加统计
 * </pre>
 */
public class LifecycleCallback implements Application.ActivityLifecycleCallbacks{

    public static LifecycleCallback getInstance() {
        return HolderClass.INSTANCE;
    }


    private final static class HolderClass {
        private final static LifecycleCallback INSTANCE = new LifecycleCallback();
    }


    private LifecycleCallback() {}

    /**
     * 必须在 Application 的 onCreate 方法中调用
     */
    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ToolLogUtils.e("Activity生命周期"+activity+"----","onActivityDestroyed");
        //将当前Activity移除到容器
    }

}
