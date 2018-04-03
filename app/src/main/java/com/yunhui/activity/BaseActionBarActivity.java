package com.yunhui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.yunhui.R;
import com.yunhui.common.Dimension;
import com.yunhui.common.StatusBarUtil;
import com.yunhui.component.NavigationBar;
import com.yunhui.util.StatusBarCompat;

/**
 * Created by pengmin on 2018/3/28.
 */

public abstract class BaseActionBarActivity extends AppCompatActivity implements NavigationBar.OnNavBarClickListener,View.OnClickListener{

    public static final String INTENT_PARAM_KEY_TITLE = "acTitle";
    public static final String INTENT_PARAM_KEY_HIDEBACKKEY = "acHideBack";

    //导航栏
    protected NavigationBar navigationBar;
    //view容器，所有子类的根布局都会添加到此容器中
    private FrameLayout baseContainer;
    //最后点击时间
    private        long lastClickTime = 0;
    //点击同一 view 最小的时间间隔，如果小于这个数则忽略此次单击。
    private static long intervalTime  = 800;
    //最后被单击的 View 的ID
    private        long lastClickView = 0;

    private boolean isModifyStatusBar = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isModifyStatusBar){
            StatusBarCompat.compat(this);
        }
        initPage(savedInstanceState);
    }

    /**
     * 初始化页面
     */
    private void initPage(Bundle savedInstanceState) {
        initActivity(savedInstanceState);
    }

    /**
     * 初始化方法,所有子类不写在onCreate方法里面
     *
     * @param savedInstanceState
     */
    protected abstract void initActivity(Bundle savedInstanceState);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);

        //初始化导航栏
        navigationBar = (NavigationBar) findViewById(R.id.navigation_bar);
        if (Build.VERSION.SDK_INT < 21) {
            navigationBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Dimension.dip2px(70, this)));
        }
        if (isStatusBarLight()){
            StatusBarUtil.setColorWithDark(this,getResources().getColor(R.color.white),true);
        }
        navigationBar.setOnNavBarClickListener(this);
        if (getIntent() != null) {
            String title = getIntent().getStringExtra(INTENT_PARAM_KEY_TITLE);
            if (title != null) {
                navigationBar.setTitle(title);
            }

            Boolean hideBack = getIntent().getBooleanExtra(INTENT_PARAM_KEY_HIDEBACKKEY, false);
            if (hideBack){
                navigationBar.setBackBtnVisibility(View.INVISIBLE);
            }
        }
        //初始化根布局容器
        baseContainer = (FrameLayout) findViewById(R.id.base_container);
        if (layoutResID != 0) {
            ViewGroup.inflate(this, layoutResID, baseContainer);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    /**
     * 状态栏背景色是否为浅色
     * 主要设置区分背景色和 图标及icon 对比设置显示。
     * @return
     */
    protected boolean isStatusBarLight() {
        return false;
    }

    /**
     * 隐藏导航栏，splash页面，主页面会用到
     */
    protected void hideNavigationBar() {
        navigationBar.setVisibility(View.GONE);
        navigationBar.setOnClickListener(this);
    }

    /**
     * 显示导航栏
     */
    protected void showNavigationBar() {
        navigationBar.setVisibility(View.VISIBLE);
    }

    /**
     * 导航栏点击事件回调
     *
     * @param navBarItem
     */
    @Override
    public void onNavItemClick(NavigationBar.NavigationBarItem navBarItem) {
        if (navBarItem == NavigationBar.NavigationBarItem.back) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        if (!isFastMultiClick(view)) {
            onViewClick(view);
        }
    }

    /**
     * 防短时重复点击回调 <br>
     * 子类使用 View.OnClickListener 设置监听事件时直接覆写该方法完成点击回调事件
     *
     * @param view 被单击的View
     */
    protected void onViewClick(View view) {
        //供字类重写用事件
    }

    /**
     * 是否快速多次点击(连续多点击）
     *
     * @param view 被点击view，如果前后是同一个view，则进行双击校验
     * @return 认为是重复点击时返回true。
     */
    private boolean isFastMultiClick(View view) {
        long time = System.currentTimeMillis() - lastClickTime;

        if (time < intervalTime && lastClickView == view.getId()) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }

        lastClickTime = System.currentTimeMillis();
        lastClickView = view.getId();

        return false;
    }
}
