package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.bean.MyEarnings;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.request.ExtractRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

/**
 * Created by pengmin on 2018/5/1.
 */

public class ExtractActivity extends BaseActionBarActivity{

    private EditText et_extractNum;
    private Button b_extractAllNum;
    private EditText et_extractWallet;
    private EditText et_extractSMS;
    private Button b_extractSendSMS;
    private Button b_extractCancle;
    private Button b_extractConfirm;
    private String extractBill;
    private TextView tv_Poundage;
    private CircleImageView civ_extracteuserphoto;
    private TextView tv_extracteuserphonenum;
    private TextView tv_extractclouddrill;
    private TextView tv_extractBTC;
    private MyEarnings myEarnings;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_extract);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryPoundage();
        allTotalRevenueRequets();
    }

    private void initView() {
        navigationBar.setTitle("BTC提取");
        navigationBar.setBackground(R.color.color_4F5051);
        et_extractNum = findViewById(R.id.extractnum);
        b_extractAllNum = findViewById(R.id.extractAllNum);
        et_extractWallet = findViewById(R.id.extractwallet);
        et_extractSMS = findViewById(R.id.extractSMS);
        b_extractSendSMS = findViewById(R.id.extractSendSMS);
        b_extractCancle = findViewById(R.id.extractcancle);
        b_extractConfirm = findViewById(R.id.extractconfirm);
        tv_Poundage = findViewById(R.id.poundage);
        civ_extracteuserphoto = findViewById(R.id.extracteuserphoto);
        tv_extracteuserphonenum = findViewById(R.id.extracteuserphonenum);
        tv_extractclouddrill = findViewById(R.id.extractclouddrill);
        tv_extractBTC = findViewById(R.id.extractBTC);
        b_extractAllNum.setOnClickListener(this);
        b_extractSendSMS.setOnClickListener(this);
        b_extractCancle.setOnClickListener(this);
        b_extractConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.extractAllNum:
                if(myEarnings != null) {
                    et_extractNum.setText(myEarnings.getBtcoin());
                }else{
                    ToastUtil.toast(ExtractActivity.this,"查询个人收益失败");
                }
                break;
            case R.id.extractSendSMS:
                break;
            case R.id.extractcancle:
                ExtractActivity.this.finish();
                break;
            case R.id.extractconfirm:
                createExtractBill();
                break;
        }
    }

    private void createExtractBill(){
        RequestUtil requestUtil = ExtractRequestFactory.createExtractBill(ExtractActivity.this,et_extractNum.getText().toString(),et_extractWallet.getText().toString());

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                extractBill = jsonObject.optString("billId");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent smsIntent = new Intent(ExtractActivity.this,SendSMSActivity.class);
                        smsIntent.putExtra("BIllID",extractBill);
                        startActivity(smsIntent);
                    }
                });
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void queryPoundage(){
        RequestUtil requestUtil = ExtractRequestFactory.querttPoundage(ExtractActivity.this,extractBill);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                final JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_Poundage.setText("您的钱包地址 (手续费:" + jsonObject.optString("fee") + "BTC)");
                    }
                });
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void allTotalRevenueRequets(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(ExtractActivity.this,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject != null && jsonObject.has("total") && jsonObject.has("btcoin")){
                    myEarnings = new MyEarnings(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_extractclouddrill.setText("云钻:" + myEarnings.getTotal());
                            tv_extractBTC.setText("BTC:" + myEarnings.getBtcoin());
                        }
                    });
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }
}
