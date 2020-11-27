package com.yc.checktool;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.BatteryManager;
import android.os.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SecurityCheckUtil {

    private static class SingletonHolder {
        private static final SecurityCheckUtil singleInstance = new SecurityCheckUtil();
    }

    private SecurityCheckUtil() {
    }

    public static final SecurityCheckUtil getSingleInstance() {
        return SingletonHolder.singleInstance;
    }

    /**
     * 检测有么有加载so库
     *
     * @param paramString
     * @return
     */
    public boolean hasReadProcMaps(String paramString) {
        try {
            Object localObject = new HashSet();
            BufferedReader localBufferedReader =
                    new BufferedReader(new FileReader("/proc/" + Process.myPid() + "/maps"));
            for (; ; ) {
                String str = localBufferedReader.readLine();
                if (str == null) {
                    break;
                }
                if ((str.endsWith(".so")) || (str.endsWith(".jar"))) {
                    ((Set) localObject).add(str.substring(str.lastIndexOf(" ") + 1));
                }
            }
            localBufferedReader.close();
            localObject = ((Set) localObject).iterator();
            while (((Iterator) localObject).hasNext()) {
                boolean bool = ((String) ((Iterator) localObject).next()).contains(paramString);
                if (bool) {
                    return true;
                }
            }
        } catch (Exception fuck) {
        }
        return false;
    }

    /**
     * java读取/proc/uid/status文件里TracerPid的方式来检测是否被调试
     *
     * @return
     */
    public boolean readProcStatus() {
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(
                    "/proc/" + Process.myPid() + "/status"));
            String tracerPid = "";
            for (; ; ) {
                String str = localBufferedReader.readLine();
                if (str.contains("TracerPid")) {
                    tracerPid = str.substring(str.indexOf(":") + 1, str.length()).trim();
                    break;
                }
                if (str == null) {
                    break;
                }
            }
            localBufferedReader.close();
            if ("0".equals(tracerPid)) return false;
            else return true;
        } catch (Exception fuck) {
            return false;
        }
    }
}
