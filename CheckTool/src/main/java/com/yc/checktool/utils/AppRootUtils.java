package com.yc.checktool.utils;

import com.yc.checktool.CommandUtil;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : root判断相关工具类
 *     revise:
 * </pre>
 */
public final class AppRootUtils {


    /**
     * 判断设备是否 root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDeviceRooted() {
        try {
            String su = "su";
            String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                    "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
            for (String location : locations) {
                if (new File(location + su).exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 检查root权限
     *
     * @return
     */
    public static boolean isRoot() {
        int secureProp = getroSecureProp();
        if (secureProp == 0){
            //eng/userdebug版本，自带root权限
            return true;
        } else {
            //user版本，继续查su文件
            return isSUExist();
        }
    }

    private static int getroSecureProp() {
        int secureProp;
        String roSecureObj = CommandUtil.getSingleInstance().getProperty("ro.secure");
        if (roSecureObj == null) {
            secureProp = 1;
        } else {
            if ("0".equals(roSecureObj)){
                secureProp = 0;
            } else {
                secureProp = 1;
            }
        }
        return secureProp;
    }

    private static int getroDebugProp() {
        int debugProp;
        String roDebugObj = CommandUtil.getSingleInstance().getProperty("ro.debuggable");
        if (roDebugObj == null) debugProp = 1;
        else {
            if ("0".equals(roDebugObj)) debugProp = 0;
            else debugProp = 1;
        }
        return debugProp;
    }

    private static boolean isSUExist() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            File file = new File(location + su);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

}
