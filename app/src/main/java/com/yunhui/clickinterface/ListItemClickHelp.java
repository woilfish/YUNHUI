package com.yunhui.clickinterface;

import android.view.View;

/**
 * ListView 点击事件接口
 */

public interface ListItemClickHelp {

    /**
     * 点击item条目中某个控件回调的方法
     * @param item ListView中item布局的View对象
     * @param parent 父容器对象
     * @param position item在ListView中所处的位置
     * @param which item中要点击的控件的id
     */
    void onClick(View item, View parent, int position, int which);
}
