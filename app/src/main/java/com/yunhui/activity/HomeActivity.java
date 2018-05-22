package com.yunhui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhui.R;
import com.yunhui.component.NavigationBar;
import com.yunhui.controller.FragmentController;
import com.yunhui.fragmenr.ConsultingFragment;
import com.yunhui.fragmenr.EarningsFragment;
import com.yunhui.fragmenr.MyFragment;
import com.yunhui.fragmenr.TaskFragment;
import com.yunhui.manager.ActivityQueueManager;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActionBarActivity implements View.OnClickListener{

    private FragmentController fragmentController;
    public FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private int currentId = 0;
    private Class fragmentArray[] = {
            ConsultingFragment.class, TaskFragment.class, EarningsFragment.class,MyFragment.class
    };
    private String mTextArray[] = {
            "资讯", "任务", "收益","我的"
    };
    private int mImageViewArray[] = {
        R.mipmap.consult,R.mipmap.task,R.mipmap.earnings,R.mipmap.my
    };
    private int mImageViewArraySelected[] = {
        R.mipmap.consult_select,R.mipmap.task_select,R.mipmap.earnings_select,R.mipmap.my_select
    };
    public boolean isNewMessage = false;

    private boolean isExit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    @Override
    protected void initActivity(Bundle savedInstanceState) {

    }

    /**
     *  初始化View
     */
    public void initView(Bundle savedInstanceState){
        setContentView(R.layout.activity_main);
        fragmentController = new FragmentController(this, R.id.fl_container);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        layoutInflater = LayoutInflater.from(this);
        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){

        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_container);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null); //设置每个TabView 的控件
            View view = mTabHost.getTabWidget().getChildAt(i);
            view.setId(i);
            view.setOnClickListener(this); //此事件会消费掉原生的点击事件，可以自己处理
        }
    }

    /**
     * 获取底部tabeView
     * @param index
     * @return
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.item_tab_view, null);
        updateTab(view, index, 0);
        return view;
    }

    private void updateTab(View view, int index, int currentId) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_tab);
        TextView textview = (TextView) view.findViewById(R.id.textview_tab);
        textview.setText(mTextArray[index]);
        try {
            if (index == currentId) {
                if(currentId == 2 && isNewMessage){
//                    imageView.setImageResource(R.drawable.tab_me_rp_select);
                }else {
                    imageView.setImageResource(mImageViewArraySelected[index]);
                }
                textview.setTextColor(getResources().getColor(R.color.color_EE9707));
            } else {
                if(index == 2 && isNewMessage){
//                    imageView.setImageResource(R.drawable.tab_me_rp_color);
                }else{
                    imageView.setImageResource(mImageViewArray[index]);
                }

                textview.setTextColor(getResources().getColor(R.color.white));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        try {
            if (view.getId() < 4 && view.getId() >= 0) {
                currentId = view.getId();
                onTabChanged(currentId);
                mTabHost.setCurrentTab(view.getId());
                switch (view.getId()) {
                    case 0://首页

                        break;
                    case 1://账务

                        break;
                    case 2://生活

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当某一tab被选中回调，更新底部图标
     *
     * @param tabId
     */
    public void onTabChanged(int tabId) {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View view = mTabHost.getTabWidget().getChildAt(i);
            updateTab(view, i, currentId);
        }

    }

    /**
     * 跳转到某一选项下，供外部调用
     *
     * @param index  0首页，1卡包，2生活，3我
     * @param bundle 传递的参数
     */
    public void toTabItem(int index, Bundle bundle) {
        currentId = index;
        onTabChanged(currentId);
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        //切换到相应界面
        mTabHost.setCurrentTab(index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    /**
     * 获取导航栏
     * @return
     */
    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exitByDoubleClick();
        }
        return false;
    }

    private void exitByDoubleClick() {
        Timer tExit=null;
        if(!isExit){
            isExit=true;
            Toast.makeText(HomeActivity.this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            ActivityQueueManager.getInstance().doFinishAll();
            System.exit(0);
        }
    }
}
