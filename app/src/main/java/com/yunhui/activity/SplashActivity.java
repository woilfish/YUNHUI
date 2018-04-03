package com.yunhui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.yunhui.R;

/**
 * Created by pengmin on 2018/4/3.
 * 应用程序启动页面
 */

public class SplashActivity extends BaseActionBarActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉标题栏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }
}
