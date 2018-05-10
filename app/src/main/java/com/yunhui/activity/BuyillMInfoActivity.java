package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.bean.ProductMachine;
import com.yunhui.bean.UserInfo;
import com.yunhui.pay.AlRunnable;
import com.yunhui.pay.PayResult;
import com.yunhui.request.BuyRequestFactory;
import com.yunhui.request.LoginRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.util.Map;

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
    private String billId;
    private String signData;
    private int buyNum = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case AlRunnable.SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(BuyillMInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BuyillMInfoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

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
        tv_allWallet.setText("合计:￥" + productMachine.getAmout());
        tv_num.setText(String.valueOf(buyNum));
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
                if(buyNum != 1){
                    buyNum --;
                    tv_num.setText(String.valueOf(buyNum));
                    tv_allWallet.setText("合计:￥" + productMachine.getAmout() * buyNum);
                }
                break;
            case R.id.add:
                if(productMachine.getAmout() == 1000){
                    if(buyNum != 50){
                        buyNum ++;
                        tv_num.setText(String.valueOf(buyNum));
                        tv_allWallet.setText("合计:￥" + productMachine.getAmout() * buyNum);
                    }else{
                        ToastUtil.toast(BuyillMInfoActivity.this,"购买数量已达今日最大值，如需购买请明天继续");
                    }
                }else if(productMachine.getAmout() == 5000){
                    if(buyNum != 10){
                        buyNum ++;
                        tv_num.setText(String.valueOf(buyNum));
                        tv_allWallet.setText("合计:￥" + productMachine.getAmout() * buyNum);
                    }else{
                        ToastUtil.toast(BuyillMInfoActivity.this,"购买数量已达今日最大值，如需购买请明天继续");
                    }
                }else if(productMachine.getAmout() == 10000){
                    if(buyNum != 5){
                        buyNum ++;
                        tv_num.setText(String.valueOf(buyNum));
                        tv_allWallet.setText("合计:￥" + productMachine.getAmout() * buyNum);
                    }
                    else{
                        ToastUtil.toast(BuyillMInfoActivity.this,"购买数量已达今日最大值，如需购买请明天继续");
                    }
                }
                break;
            case R.id.pay:
                //创建订单
                createPayBill();
                break;
        }
    }

    private void createPayBill(){

        RequestUtil requestUtil = BuyRequestFactory.createPayBill(BuyillMInfoActivity.this,String.valueOf(0.01 * buyNum),"CZ",productMachine.getId(),tv_num.getText().toString());
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                billId = jsonObject.optString("billId");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alPay();
                    }
                });
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
                Toast.makeText(BuyillMInfoActivity.this,"创建支付订单失败，请重试",Toast.LENGTH_SHORT);
            }
        });
        requestUtil.execute();
    }

    private void alPay(){
        RequestUtil requestUtil = BuyRequestFactory.doAlPay(BuyillMInfoActivity.this,billId);
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                signData = jsonObject.optString("signData");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlRunnable alRunnable = new AlRunnable(BuyillMInfoActivity.this,handler,signData);
                        Thread payThread = new Thread(alRunnable);
                        payThread.start();
                    }
                });
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
                Toast.makeText(BuyillMInfoActivity.this,"创建支付订单失败，请重试",Toast.LENGTH_SHORT);
            }
        });
        requestUtil.execute();
    }
}
