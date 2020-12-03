package io.virtualapp.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.launcher3.LauncherFiles;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.DeviceUtil;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.MD5Utils;

import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.models.AppInfo;
import io.virtualapp.home.models.AppInfoLite;
import io.virtualapp.home.repo.AppRepository;
import io.virtualapp.settings.SettingsActivity;
import io.virtualapp.sys.Installd;
import io.virtualapp.update.VAVersionService;
import jonathanfinerty.once.Once;

import static io.virtualapp.XApp.XPOSED_INSTALLER_PACKAGE;

/**
 * @author weishu
 * @date 18/2/9.
 */

public class HomeActivity extends Activity {

    private static final String SHOW_DOZE_ALERT_KEY = "SHOW_DOZE_ALERT_KEY";
    private Handler mUiHandler;
    private boolean mDirectlyBack = false;
    private TextView mTvOpen;
    private final AtomicBoolean checkXposedInstaller = new AtomicBoolean(true);
    private final AtomicBoolean checkWeChetInstaller = new AtomicBoolean(true);

    public static void goHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(LauncherFiles.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        mTvOpen = findViewById(R.id.tv_open);
        setWindow();
        showMenuKey();
        mUiHandler = new Handler(getMainLooper());
        alertForMeizu();
        alertForDonate();
        mDirectlyBack = sharedPreferences.getBoolean(SettingsActivity.DIRECTLY_BACK_KEY, false);
        mTvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isWeChatInstalled = VirtualCore.get().isAppInstalled(WeChat_INSTALLER_PACKAGE);
                if (isWeChatInstalled){
                    LoadingActivity.launch(getApplicationContext(), WeChat_INSTALLER_PACKAGE, 0);
                    return;
                }
                Toast.makeText(HomeActivity.this,"请重启挂载微信",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setWindow() {
        // 全屏展示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
                this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                // 全屏显示，隐藏状态栏
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }

    private void installXposed() {
        if (!VirtualCore.get().isXposedEnabled()) {
            return;
        }
        boolean isXposedInstalled = false;
        try {
            isXposedInstalled = VirtualCore.get().isAppInstalled(XPOSED_INSTALLER_PACKAGE);
            File oldXposedInstallerApk = getFileStreamPath("XposedInstaller_1_31.apk");
            if (oldXposedInstallerApk.exists()) {
                VirtualCore.get().uninstallPackage(XPOSED_INSTALLER_PACKAGE);
                oldXposedInstallerApk.delete();
                isXposedInstalled = false;
            }
        } catch (Throwable e) {
        }

        if (!isXposedInstalled) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("正在初始化");
            dialog.show();

            VUiKit.defer().when(() -> {
                File xposedInstallerApk = getFileStreamPath("XposedInstaller_5_8.apk");
                if (!xposedInstallerApk.exists()) {
                    InputStream input = null;
                    OutputStream output = null;
                    try {
                        input = getApplicationContext().getAssets().open("XposedInstaller_3.1.5.apk_");
                        output = new FileOutputStream(xposedInstallerApk);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                    } catch (Throwable e) {
                    } finally {
                        FileUtils.closeQuietly(input);
                        FileUtils.closeQuietly(output);
                    }
                }

                if (xposedInstallerApk.isFile() && !DeviceUtil.isMeizuBelowN()) {
                    try {
                        if ("8537fb219128ead3436cc19ff35cfb2e".equals(MD5Utils.getFileMD5String(xposedInstallerApk))) {
                            VirtualCore.get().installPackage(xposedInstallerApk.getPath(), InstallStrategy.TERMINATE_IF_EXIST);
                        } else {
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }).then((v) -> {
                dismissDialog(dialog);
            }).fail((err) -> {
                dismissDialog(dialog);
            });
        }
    }


    /**
     * 安装微信
     */
    public static final String WeChat_INSTALLER_PACKAGE = "com.tencent.mm";
    private void installWeChat() {
        if (!VirtualCore.get().isXposedEnabled()) {
            return;
        }
        boolean isXposedInstalled = false;
        try {
            isXposedInstalled = VirtualCore.get().isAppInstalled(WeChat_INSTALLER_PACKAGE);
            File oldXposedInstallerApk = getFileStreamPath("weixin703.apk");
            if (oldXposedInstallerApk.exists()) {
                VirtualCore.get().uninstallPackage(WeChat_INSTALLER_PACKAGE);
                oldXposedInstallerApk.delete();
                isXposedInstalled = false;
            }
        } catch (Throwable e) {
        }

        if (!isXposedInstalled) {
            VUiKit.defer().when(() -> {
                File xposedInstallerApk = getFileStreamPath("weixin703.apk");
                if (!xposedInstallerApk.exists()) {
                    InputStream input = null;
                    OutputStream output = null;
                    try {
                        input = getApplicationContext().getAssets().open("weixin703.apk_");
                        output = new FileOutputStream(xposedInstallerApk);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                    } catch (Throwable e) {
                    } finally {
                        FileUtils.closeQuietly(input);
                        FileUtils.closeQuietly(output);
                    }
                }

                if (xposedInstallerApk.isFile() && !DeviceUtil.isMeizuBelowN()) {
                    try {
                        VirtualCore.get().installPackage(xposedInstallerApk.getPath(), InstallStrategy.TERMINATE_IF_EXIST);
                    } catch (Throwable ignored) {
                    }
                }
            }).then((v) -> {
            }).fail((err) -> {
            });
        }
    }


    private static void dismissDialog(ProgressDialog dialog) {
        if (dialog == null) {
            return;
        }
        try {
            dialog.dismiss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkXposedInstaller.compareAndSet(true, false)) {
            installXposed();
        }
        if (checkWeChetInstaller.compareAndSet(true, false)) {
            //installWeChat();
            userWeixin();
        }
        // check for update
        new Handler().postDelayed(() ->
                VAVersionService.checkUpdate(getApplicationContext(), false), 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            onSettingsClicked();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Activity getActivity() {
        return this;
    }

    public Context getContext() {
        return this;
    }

    private void onAddAppClicked() {
        //ListAppActivity.gotoListApp(this);
        onSettingsClicked();
    }

    private void onSettingsClicked() {
        //点击去设置中心
        //startActivity(new Intent(NewHomeActivity.this, SettingsActivity.class));

        boolean isWeChatInstalled = VirtualCore.get().isAppInstalled(WeChat_INSTALLER_PACKAGE);
        if (isWeChatInstalled){
            LoadingActivity.launch(getApplicationContext(), WeChat_INSTALLER_PACKAGE, 0);
            return;
        }
        Toast.makeText(this,"请重启挂载微信",Toast.LENGTH_SHORT).show();
    }

    private void alertForDonate() {
        final String TAG = "show_donate";
        if (Once.beenDone(Once.THIS_APP_VERSION, TAG)) {
            alertForDoze();
        }
    }

    private void alertForMeizu() {
        if (!DeviceUtil.isMeizuBelowN()) {
            return;
        }
        boolean isXposedInstalled = VirtualCore.get().isAppInstalled(XPOSED_INSTALLER_PACKAGE);
        if (isXposedInstalled) {
            return;
        }
        mUiHandler.postDelayed(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.meizu_device_tips_title)
                    .setMessage(R.string.meizu_device_tips_content)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    })
                    .create();
            try {
                alertDialog.show();
            } catch (Throwable ignored) {
            }
        }, 2000);
    }

    private void alertForDoze() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager == null) {
            return;
        }
        boolean showAlert = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SHOW_DOZE_ALERT_KEY, true);
        if (!showAlert) {
            return;
        }
        String packageName = getPackageName();
        boolean ignoringBatteryOptimizations = powerManager.isIgnoringBatteryOptimizations(packageName);
        if (!ignoringBatteryOptimizations) {
            mUiHandler.postDelayed(() -> {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_for_doze_mode_title)
                        .setMessage(R.string.alert_for_doze_mode_content)
                        .setPositiveButton(R.string.alert_for_doze_mode_yes, (dialog, which) -> {
                            try {
                                startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getPackageName())));
                            } catch (ActivityNotFoundException ignored) {
                                // ActivityNotFoundException on some devices.
                                try {
                                    startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
                                } catch (Throwable e) {
                                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .edit().putBoolean(SHOW_DOZE_ALERT_KEY, false).apply();
                                }
                            } catch (Throwable e) {
                                PreferenceManager.getDefaultSharedPreferences(getActivity())
                                        .edit().putBoolean(SHOW_DOZE_ALERT_KEY, false).apply();
                            }
                        })
                        .setNegativeButton(R.string.alert_for_doze_mode_no, (dialog, which) ->
                                PreferenceManager.getDefaultSharedPreferences(getActivity())
                                        .edit().putBoolean(SHOW_DOZE_ALERT_KEY, false).apply())
                        .create();
                try {
                    alertDialog.show();
                } catch (Throwable ignored) {
                    ignored.printStackTrace();
                }
            }, 1000);
        }
    }

    private void showMenuKey() {
        try {
            Method setNeedsMenuKey = Window.class.getDeclaredMethod("setNeedsMenuKey", int.class);
            setNeedsMenuKey.setAccessible(true);
            int value = WindowManager.LayoutParams.class.getField("NEEDS_MENU_SET_TRUE").getInt(null);
            setNeedsMenuKey.invoke(getWindow(), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userWeixin() {
        boolean isWeChatInstalled = VirtualCore.get().isAppInstalled(WeChat_INSTALLER_PACKAGE);
        if (isWeChatInstalled){
            //Toast.makeText(this,"微信挂载成功",Toast.LENGTH_SHORT).show();
            LoadingActivity.launch(getApplicationContext(), WeChat_INSTALLER_PACKAGE, 0);
            return;
        }
        AppRepository mRepository = new AppRepository(this);
        Promise<List<AppInfo>, Throwable, Void> installedApps = mRepository.getInstalledApps(this);
        installedApps.done(new DoneCallback<List<AppInfo>>() {
            @Override
            public void onDone(List<AppInfo> result) {
                for (int i=0 ; i<result.size() ; i++){
                    AppInfo appInfo = result.get(i);
                    if (appInfo.packageName.equals(WeChat_INSTALLER_PACKAGE)){
                        ArrayList<AppInfoLite> dataList = new ArrayList<>();
                        dataList.add(new AppInfoLite(appInfo.packageName,
                                appInfo.path, appInfo.fastOpen, appInfo.disableMultiVersion));
                        //挂载微信
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Activity activity = getActivity();
                                if (activity == null) {
                                    return;
                                }
                                Installd.startInstallerActivity(activity, dataList);
                                activity.setResult(Activity.RESULT_OK);
                            }
                        }).start();
                        break;
                    }
                }
            }
        });
    }

}
