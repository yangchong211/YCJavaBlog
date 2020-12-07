package com.yc.xposeddemo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTvText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mTvText = findViewById(R.id.tv_text);
        mTvText.setText("模拟插件测试工具");
        //setText("模拟插件测试工具");
    }

    private void setText(CharSequence charSequence){
        mTvText.setText(charSequence);
    }

}
