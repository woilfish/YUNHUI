package com.yunhui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yunhui.R;
import com.yunhui.adapter.GuideAdapter;
import com.yunhui.manager.ActivityQueueManager;

/**
 * Created by pengmin on 2018/4/3.
 * 引导页面
 */

public class GuideActivity extends BaseActionBarActivity implements ViewPager.OnPageChangeListener,GuideAdapter.EnterHomeCallBack{

    private Context mContext;
    private ViewPager viewPager;
    private GuideAdapter guideAdapter;
    private LinearLayout mDotContainer;

    private int displayWidth = 0,displayHeight = 0 ;
    private DisplayMetrics displayMetrics;
    private float density;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        displayMetrics = getResources().getDisplayMetrics();
        density = displayMetrics.density;
        setContentView(R.layout.activity_guide);
        hideNavigationBar();
        viewPager = (ViewPager) findViewById(R.id.activity_guide_viewpager);
        mDotContainer   = (LinearLayout) findViewById(R.id.dotContainer);
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == guideAdapter) {
            initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    /**
     * 初始化UI
     */
    private void initView(){
        Rect outRect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        displayHeight = outRect.height();
        displayWidth = outRect.width();

        guideAdapter = new GuideAdapter(mContext,this);
        viewPager.setAdapter(guideAdapter);
        viewPager.setOnPageChangeListener(this);

        setBottom(0);
    }

    @Override
    protected void initActivity(Bundle savedInstanceState) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setBottom(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void enterHomeActivity() {

    }

    /**
     * 设置底部index
     * @param position
     */
    private void setBottom(int position) {
        int size;
        int[] bottomIcons;
        size = guideAdapter.getCount();
        bottomIcons = GuideAdapter.ORIGINAL_BOTTOM_IMAGE_IDS;
        position %= guideAdapter.getCount();;
        if (position < 0) {
            position = size + position;
        }
        if (size < 2) {
            return;
        }
        if (mDotContainer.getChildCount() == 0) {
            int density = (int) getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams lpDot = new LinearLayout.LayoutParams(density*13, density*13);
            for (int i = 0; i < size; i++) {
                ImageView v = new ImageView(mContext);
                v.setLayoutParams(lpDot);
                v.setPadding(density*3, 0, density*3, 0);
                mDotContainer.addView(v);
            }
        }
        for (int i = 0; i < size; i++) {
            ImageView v = (ImageView) mDotContainer.getChildAt(i);
            v.setImageResource(position == i ? bottomIcons[0] : bottomIcons[1]);
        }
        mDotContainer.setVisibility(position == size - 1 ? View.GONE : View.VISIBLE);
    }
}
