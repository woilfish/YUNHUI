package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.ProductMachine;

/**
 * Created by pengmin on 2018/5/2.
 * 购买详情页面
 */

public class BuyillMInfoActivity extends BaseActionBarActivity{


    private TextView tv_buyMillTitle;
    private TextView tv_buyRarningsInfo;
    private TextView tv_buyRarning;
    private TextView tv_buyRarningDay;
    private TextView tv_buyRarningDayInfo;
    private TextView tv_millSinge;
    private Button b_reduction;
    private TextView tv_num;
    private Button b_add;
    private TextView tv_allWallet;
    private Button b_pay;
    private ProductMachine productMachine;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buymill_info);
        productMachine = (ProductMachine) getIntent().getSerializableExtra("millInfo");
        initView();
        initData();
    }

    private void initData() {
        navigationBar.setTitle("购买云钻矿机");
        navigationBar.setBackground(R.color.color_4F5051);
        tv_buyMillTitle.setText(productMachine.getTitle());
        tv_buyRarningsInfo.setText(productMachine.getDayBenifit());
        tv_buyRarning.setText(productMachine.getTotalBenifit());
        tv_buyRarningDay.setText(productMachine.getCircle());
        tv_buyRarningDayInfo.setText(productMachine.getContent());
        tv_millSinge.setText("每台单价:￥" + productMachine.getAmout());
    }

    private void initView() {
        tv_buyMillTitle = findViewById(R.id.buymilltitle);
        tv_buyRarningsInfo = findViewById(R.id.buyrarningsinfo);
        tv_buyRarning = findViewById(R.id.buyrarning);
        tv_buyRarningDay = findViewById(R.id.buyrarningday);
        tv_buyRarningDayInfo = findViewById(R.id.buyrarningdayinfo);
        tv_millSinge = findViewById(R.id.millsinge);
        b_reduction = findViewById(R.id.reduction);
        tv_num = findViewById(R.id.num);
        b_add = findViewById(R.id.add);
        tv_allWallet = findViewById(R.id.allWallet);
        b_pay = findViewById(R.id.pay);
        b_add.setOnClickListener(this);
        b_reduction.setOnClickListener(this);
        b_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.reduction:
                break;
            case R.id.add:
                break;
            case R.id.pay:
                break;
        }
    }
}
