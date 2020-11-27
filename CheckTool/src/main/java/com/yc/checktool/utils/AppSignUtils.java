package com.yc.checktool.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 签名相关工具类
 *     revise:
 * </pre>
 */
public final class AppSignUtils {

    /**
     * 获取签名信息
     * @param context                               上下文
     * @return
     */
    public static String getSignature(Context context) {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            // 通过返回的包信息获得签名数组
            Signature[] signatures = packageInfo.signatures;
            // 循环遍历签名数组拼接应用签名
            StringBuilder builder = new StringBuilder();
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            // 得到应用签名
            return builder.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


}
