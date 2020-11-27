package com.yc.checktool.utils;

import android.os.Process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 安全 工具类
 *     revise:
 * </pre>
 */
public final class SecurityUtils {

    /**
     * 检测有么有加载so库
     *
     * @param paramString
     * @return
     */
    public static boolean hasReadProcMaps(String paramString) {
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
    public static boolean readProcStatus() {
        try {
            int myPid = Process.myPid();
            FileReader fileReader = new FileReader("/proc/" + myPid + "/status");
            BufferedReader localBufferedReader = new BufferedReader(fileReader);
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
            if ("0".equals(tracerPid)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception fuck) {
            return false;
        }
    }
}
