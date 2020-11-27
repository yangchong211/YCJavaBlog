package com.yc.checktool;

import android.content.Context;

import com.yc.checktool.utils.AppRootUtils;
import com.yc.checktool.utils.AppSignUtils;
import com.yc.checktool.utils.AppToolUtils;
import com.yc.checktool.utils.XposedUtils;

import java.net.UnknownHostException;


public class EasyProtectorLib {

    public static String checkSignature(Context context) {
        return AppSignUtils.getSignature(context);
    }

    public static boolean checkIsDebug(Context context) {
        return AppToolUtils.checkIsDebugVersion(context) || AppToolUtils.checkIsDebuggerConnected();
    }

    public static boolean checkIsPortUsing(String host, int port) {
        try {
            return AppToolUtils.isPortUsing(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean checkIsRoot() {
        return AppRootUtils.isRoot();
    }

    public static boolean checkIsXposedExist() {
        return XposedUtils.isXposedExistByThrow();
    }

    public static boolean checkXposedExistAndDisableIt() {
        return XposedUtils.tryShutdownXposed();
    }

    public static boolean checkHasLoadSO(String soName) {
        return SecurityCheckUtil.getSingleInstance().hasReadProcMaps(soName);
    }

    public static boolean checkIsBeingTracedByJava() {
        return SecurityCheckUtil.getSingleInstance().readProcStatus();
    }

    public static void checkIsBeingTracedByC() {
//        NDKUtil.loadLibrariesOnce(null);
//        NDKUtil.loadLibraryByName("antitrace");
    }

    public static boolean checkIsRunningInEmulator(Context context, EmulatorCheckCallback callback) {
        return EmulatorCheckUtil.getSingleInstance().readSysProperty(context, callback);
    }

    public static boolean checkIsRunningInVirtualApk(String uniqueMsg, VirtualCheckCallback callback) {
        return VirtualApkCheckUtil.getSingleInstance().checkByCreateLocalServerSocket(uniqueMsg, callback);
    }
}
