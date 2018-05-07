package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.request.ExtractRequestFactory;
import com.yunhui.request.RequestUtil;

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

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_extract);
        initView();
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
}
