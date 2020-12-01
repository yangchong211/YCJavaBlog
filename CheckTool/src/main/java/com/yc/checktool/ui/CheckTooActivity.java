package com.yc.checktool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.checktool.R;
import com.yc.checktool.help.CheckAppTool;
import com.yc.checktool.inter.EmulatorCallback;
import com.yc.checktool.inter.VirtualCallback;
import com.yc.checktool.utils.VirtualApkUtils;
import com.yc.checktool.utils.XposedUtils;

public class CheckTooActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;
    private TextView mTv8;
    private TextView mTv9;
    private TextView mTv10;
    private TextView mTv11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_tool);

        initView();
        initListener();
    }

    private void initView() {
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);
        mTv5 = findViewById(R.id.tv_5);
        mTv6 = findViewById(R.id.tv_6);
        mTv7 = findViewById(R.id.tv_7);
        mTv8 = findViewById(R.id.tv_8);
        mTv9 = findViewById(R.id.tv_9);
        mTv10 = findViewById(R.id.tv_10);
        mTv11 = findViewById(R.id.tv_11);
    }


    private void initListener() {
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
        mTv5.setOnClickListener(this);
        mTv6.setOnClickListener(this);
        mTv7.setOnClickListener(this);
        mTv8.setOnClickListener(this);
        mTv9.setOnClickListener(this);
        mTv10.setOnClickListener(this);
        mTv11.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_1) {
            boolean b = CheckAppTool.getInstance().IsRoot();
            if (b){
                toast("已经root");
            } else {
                toast("未被root");
            }
        } else if (v.getId() == R.id.tv_2){
            startActivity(new Intent(this,AppInfoActivity.class));
        } else if (v.getId() == R.id.tv_3){
            boolean b = VirtualApkUtils.getSingleInstance().checkByOriginApkPackageName(this, new VirtualCallback() {
                @Override
                public void findSuspect() {

                }
            });
            if (b){
                toast("是双开");
            } else {
                toast("不是双开");
            }
        } else if (v.getId() == R.id.tv_4){
            String b = CheckAppTool.getInstance().getSignature(this);
            toast(b);
        } else if (v.getId() == R.id.tv_5){
            boolean b = CheckAppTool.getInstance().checkIsDebug(this);
            if (b){
                toast("是debug");
            } else {
                toast("不是debug");
            }
        } else if (v.getId() == R.id.tv_6){
            boolean b = CheckAppTool.getInstance().checkIsXposedExist();
            if (b){
                toast("有Xposed环境");
            } else {
                toast("没有Xposed环境");
            }
        } else if (v.getId() == R.id.tv_7){
            if (!XposedUtils.isXposedExistByThrow()){
                toast("没有Xposed环境");
                return;
            }
            boolean b = CheckAppTool.getInstance().checkXposedExistAndDisableIt();
            if (b){
                toast("关闭Xposed环境");
            } else {
                toast("关闭Xposed失败");
            }
        } else if (v.getId() == R.id.tv_8){
            CheckAppTool.getInstance().isRunningInEmulator(this, new EmulatorCallback() {
                @Override
                public void findEmulator(String emulatorInfo) {
                    toast(emulatorInfo);
                }
            });
        }  else if (v.getId() == R.id.tv_9){
            boolean haha = CheckAppTool.getInstance().hasLoadSO("haha");
            if (haha){
                toast("有加载");
            } else {
                toast("未加载");
            }
        } else if (v.getId() == R.id.tv_10){
            boolean b = VirtualApkUtils.getSingleInstance().checkByHasSameUid(new VirtualCallback() {
                @Override
                public void findSuspect() {

                }
            });
            if (b){
                toast("有加载");
            } else {
                toast("未加载");
            }
        } else if (v.getId() == R.id.tv_11){

        }
    }

    private void toast(String string){
        if (string!=null && string.length()>0){
            Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"内容为空",Toast.LENGTH_SHORT).show();
        }
    }
}
