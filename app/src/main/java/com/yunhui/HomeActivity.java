package com.yunhui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.yunhui.component.NavigationBar;
import com.yunhui.controller.FragmentController;
import com.yunhui.fragmenr.ConsultingFragment;
import com.yunhui.fragmenr.EarningsFragment;
import com.yunhui.fragmenr.MyFragment;
import com.yunhui.fragmenr.TaskFragment;

public class HomeActivity extends BaseActionBarActivity implements View.OnClickListener{

    private FragmentController fragmentController;
    public FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private int currentId = 0;
    private Class fragmentArray[] = {
            ConsultingFragment.class, TaskFragment.class, EarningsFragment.class,MyFragment.class
    };
    private String mTextArray[] = {
            "咨询", "任务", "收益","我的"
    };
    private int mImageViewArray[] = {

    };
    private int mImageViewArraySelected[] = {

    };
    public boolean isNewMessage = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
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
                textview.setTextColor(getResources().getColor(R.color.AppTheThemeColor));
            } else {
                if(index == 2 && isNewMessage){
//                    imageView.setImageResource(R.drawable.tab_me_rp_color);
                }else{
                    imageView.setImageResource(mImageViewArray[index]);
                }

                textview.setTextColor(getResources().getColor(R.color.gray_999999));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
     * 获取导航栏
     * @return
     */
    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
}
