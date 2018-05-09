package com.yunhui.activity;

import android.os.Bundle;

import com.yunhui.R;

/**
 * Created by pengmin on 2018/5/1.
 * 兑换页面
 */

public class ExchangeResultActivity extends BaseActionBarActivity{

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange_result);
        initView();
    }

    private void initView() {
        navigationBar.setTitle("BTC兑换结果");
        navigationBar.setBackground(R.color.color_4F5051);
    }
}
