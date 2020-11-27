package com.yc.checktool.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public final class AppToolUtils {


    /**
     * java法检测是否连上调试器
     * @return
     */
    public static boolean checkIsDebuggerConnected() {
        return android.os.Debug.isDebuggerConnected();
    }

    /**
     * 检测app是否为debug版本
     * @param context                               上下文
     * @return
     */
    public static boolean checkIsDebugVersion(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * usb充电辅助判断
     * @param context                               上下文
     * @return
     */
    public static boolean checkIsUsbCharging(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);
        if (batteryStatus == null){
            return false;
        }
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
    }


    /**
     * 检测本地端口是否被占用
     * @param port                                  端口
     * @return
     */
    public static boolean isLocalPortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return flag;
    }

    /**
     * 检测任一端口是否被占用
     * @param host                                  host
     * @param port                                  端口
     * @return
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return flag;
    }

    /**
     * 复制内容到剪切板
     * @param context                               上下文
     * @param content                               内容
     */
    public static void copyToClipBoard(Context context , String content){
        if (!TextUtils.isEmpty(content)){
            //获取剪贴版
            ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            //创建ClipData对象
            //第一个参数只是一个标记，随便传入。
            //第二个参数是要复制到剪贴版的内容
            ClipData clip = ClipData.newPlainText("", content);
            //传入clipdata对象.
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
