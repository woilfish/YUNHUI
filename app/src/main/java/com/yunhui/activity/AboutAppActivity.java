package com.yunhui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.util.AppUtil;

/**
 * Created by pengmin on 2018/5/18.
 */

public class AboutAppActivity extends BaseActionBarActivity{


    private TextView tv_Version;
    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_app);
        initView();
    }

    private void initView() {
        navigationBar.setTitle("关于我们");
        navigationBar.setBackground(R.color.color_4F5051);
        tv_Version = findViewById(R.id.version);
        tv_Version.setText("云慧助手 " + AppUtil.getAppVersionName(AboutAppActivity.this));
    }
}
