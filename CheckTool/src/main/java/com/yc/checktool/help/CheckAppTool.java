package com.yc.checktool.help;

import android.app.Application;
import android.content.Context;

import com.yc.checktool.inter.EmulatorCallback;
import com.yc.checktool.inter.VirtualCallback;
import com.yc.checktool.utils.AppRootUtils;
import com.yc.checktool.utils.AppSignUtils;
import com.yc.checktool.utils.AppToolUtils;
import com.yc.checktool.utils.EmulatorUtils;
import com.yc.checktool.utils.SecurityUtils;
import com.yc.checktool.utils.VirtualApkUtils;
import com.yc.checktool.utils.XposedUtils;

import java.net.UnknownHostException;

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

    /**
     * 获取签名
     * @param context                           上下文
     * @return
     */
    public String getSignature(Context context) {
        return AppSignUtils.getSignature(context);
    }

    /**
     * 判断是否是debug环境
     * @param context                           上下文
     * @return
     */
    public boolean checkIsDebug(Context context) {
        return AppToolUtils.checkIsDebugVersion(context) || AppToolUtils.checkIsDebuggerConnected();
    }

    /**
     * 检测任一端口是否被占用
     * @param host                                  host
     * @param port                                  端口
     * @return
     */
    public boolean isPortUsing(String host, int port) {
        try {
            return AppToolUtils.isPortUsing(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 检查root权限
     * @return
     */
    public boolean IsRoot() {
        return AppRootUtils.isRoot();
    }

    /**
     * 通过主动抛出异常，检查堆栈信息来判断是否存在XP框架
     * @return
     */
    public boolean checkIsXposedExist() {
        return XposedUtils.isXposedExistByThrow();
    }

    /**
     * 判断是否有XP框架，并且尝试关闭它
     * @return
     */
    public boolean checkXposedExistAndDisableIt() {
        return XposedUtils.tryShutdownXposed();
    }

    /**
     * 检测有么有加载so库
     * @param soName                        检测是否有加载某so库
     * @return
     */
    public boolean hasLoadSO(String soName) {
        return SecurityUtils.hasReadProcMaps(soName);
    }

    public boolean isBeingTracedByJava() {
        return SecurityUtils.readProcStatus();
    }

    /**
     * 是否运行在模拟器上
     * @param context                       上下文
     * @param callback                      回调接口
     * @return
     */
    public boolean isRunningInEmulator(Context context, EmulatorCallback callback) {
        return EmulatorUtils.getSingleInstance().readSysProperty(context, callback);
    }

    /**
     * 是否运行在VirtualApk上
     * @param uniqueMsg
     * @param callback
     * @return
     */
    public boolean isRunningInVirtualApk(String uniqueMsg, VirtualCallback callback) {
        return VirtualApkUtils.getSingleInstance().checkByCreateLocalServerSocket(uniqueMsg, callback);
    }

}
