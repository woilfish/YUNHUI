package com.yunhui.fragmenr;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengmin on 2018/4/2.
 * 基类Fragement
 */

public class BaseFragment  extends Fragment implements View.OnClickListener{

    private String currentFragment;
    private static final Map<String, String> statisticMap = new HashMap<>();
    //最后点击时间
    private        long lastClickTime = 0;
    //最后被单击的 View 的ID
    private        long lastClickView = 0;
    //点击同一 view 最小的时间间隔，如果小于这个数则忽略此次单击。
    private static long intervalTime  = 800;

    static {
        statisticMap.put(ConsultingFragment.class.getSimpleName(), "ConsultingFragment");
        statisticMap.put(TaskFragment.class.getSimpleName(), "TaskFragment");
        statisticMap.put(EarningsFragment.class.getSimpleName(), "EarningsFragment");
        statisticMap.put(MyFragment.class.getSimpleName(), "MyFragment");
    }

    @Override
    public void onResume() {
        currentFragment = this.getClass().getSimpleName();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
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
