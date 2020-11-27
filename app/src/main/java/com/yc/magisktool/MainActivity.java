package com.yc.magisktool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yc.checktool.ui.CheckTooActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFindViewById();
        initListener();
    }

    private void initFindViewById() {
        mTv1 = findViewById(R.id.tv_1);
    }

    private void initListener() {
        mTv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this, CheckTooActivity.class));
                break;
        }
    }
}