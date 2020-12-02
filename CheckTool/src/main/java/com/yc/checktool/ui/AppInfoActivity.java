package com.yc.checktool.ui;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yc.checktool.utils.AppRootUtils;
import com.yc.checktool.utils.AppToolUtils;
import com.yc.checktool.BuildConfig;
import com.yc.checktool.help.CheckAppTool;
import com.yc.checktool.R;
import com.yc.checktool.utils.AppDeviceUtils;
import com.yc.checktool.utils.AppProcessUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 获取设备相关信息
 *     revise: 用户收集相关信息。比如：机型、系统、厂商、CPU、ABI、Linux 版本等。
 * </pre>
 */
public class AppInfoActivity extends AppCompatActivity {

    private TextView mTvPhoneContent;
    private TextView mTvAppInfo;
    private TextView mTvContentInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);

        initView();
        //手机设备信息
        setPhoneInfo();
        //设置手机信息
        setAppInfo();
        //本机信息
        //比如mac地址，子网掩码，ip，wifi名称
        setLocationInfo();
    }

    private void initView() {
        mTvPhoneContent = findViewById(R.id.tv_phone_content);
        mTvAppInfo = findViewById(R.id.tv_app_info);
        mTvContentInfo = findViewById(R.id.tv_content_info);
    }

    private void setPhoneInfo() {
        Application application = CheckAppTool.getInstance().getApplication();
        final StringBuilder sb = new StringBuilder();
        sb.append("该App信息:");
        String currentProcessName = AppProcessUtils.getCurrentProcessName(this);
        if (currentProcessName!=null){
            sb.append("\nApp进程名称:").append(currentProcessName);
        }
        sb.append("是否root:").append(AppRootUtils.isDeviceRooted());
        sb.append("\n系统硬件商:").append(AppDeviceUtils.getManufacturer());
        sb.append("\n设备的品牌:").append(AppDeviceUtils.getBrand());
        sb.append("\n手机的型号:").append(AppDeviceUtils.getModel());
        sb.append("\n设备版本号:").append(AppDeviceUtils.getId());
        sb.append("\nCPU的类型:").append(AppDeviceUtils.getCpuType());
        sb.append("\n系统的版本:").append(AppDeviceUtils.getSDKVersionName());
        sb.append("\n系统版本值:").append(AppDeviceUtils.getSDKVersionCode());
        sb.append("\nSd卡剩余控件:").append(AppDeviceUtils.getSDCardSpace(application));
        sb.append("\n系统剩余控件:").append(AppDeviceUtils.getRomSpace(application));
        sb.append("\n手机总内存:").append(AppDeviceUtils.getTotalMemory(application));
        sb.append("\n手机可用内存:").append(AppDeviceUtils.getAvailMemory(application));
        sb.append("\n手机分辨率:").append(AppDeviceUtils.getWidthPixels(application))
                .append("x").append(AppDeviceUtils.getRealHeightPixels(application));
        sb.append("\n屏幕尺寸:").append(AppDeviceUtils.getScreenInch(this));
        mTvPhoneContent.setText(sb.toString());
        mTvPhoneContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToolUtils.copyToClipBoard(AppInfoActivity.this,sb.toString());
            }
        });
    }

    private void setAppInfo() {
        Application application = CheckAppTool.getInstance().getApplication();
        //版本信息
        String versionName = "";
        String versionCode = "";
        try {
            PackageManager pm = application.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(application.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = String.valueOf(pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("软件App包名:").append(CheckAppTool.getInstance().getApplication().getPackageName());
        sb.append("\n是否是DEBUG版本:").append(BuildConfig.BUILD_TYPE);
        if (versionName!=null && versionName.length()>0){
            sb.append("\n版本名称:").append(versionName);
            sb.append("\n版本号:").append(versionCode);
        }
        ApplicationInfo applicationInfo = application.getApplicationInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb.append("\n最低系统版本号:").append(applicationInfo.minSdkVersion);
            sb.append("\n当前系统版本号:").append(applicationInfo.targetSdkVersion);
            sb.append("\n进程名称:").append(applicationInfo.processName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.append("\nUUID:").append(applicationInfo.storageUuid);
            }
            sb.append("\nAPK完整路径:").append(applicationInfo.sourceDir);
        }
        mTvAppInfo.setText(sb.toString());
    }


    private void setLocationInfo() {
        Application application = CheckAppTool.getInstance().getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("wifi信号强度:").append(AppDeviceUtils.getWifiState(application));
        sb.append("\nAndroidID:").append(AppDeviceUtils.getAndroidID(application));
        sb.append("\nMac地址:").append(AppDeviceUtils.getMacAddress(application));
        sb.append("\nWifi名称:").append(AppDeviceUtils.getWifiName(application));
        int wifiIp = AppDeviceUtils.getWifiIp(application);
        String ip = AppDeviceUtils.intToIp(wifiIp);
        sb.append("\nWifi的Ip地址:").append(ip);
        DhcpInfo dhcpInfo = AppDeviceUtils.getDhcpInfo(application);
        if (dhcpInfo!=null){
            //sb.append("\nipAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.ipAddress));
            sb.append("\n子网掩码地址：").append(AppDeviceUtils.intToIp(dhcpInfo.netmask));
            sb.append("\n网关地址：").append(AppDeviceUtils.intToIp(dhcpInfo.gateway));
            sb.append("\nserverAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.serverAddress));
            sb.append("\nDns1：").append(AppDeviceUtils.intToIp(dhcpInfo.dns1));
            sb.append("\nDns2：").append(AppDeviceUtils.intToIp(dhcpInfo.dns2));
        }
        mTvContentInfo.setText(sb.toString());
    }

}
