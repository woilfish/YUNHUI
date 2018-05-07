package com.yunhui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.request.ExtractRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.StringUtil;

/**
 * Created by pengmin on 2018/5/7.
 */

public class SendSMSActivity extends BaseActionBarActivity{

    private EditText et_SMS;
    private Button b_SendSMS;
    private Button b_doneSMS;
    private String billId;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_sms);
        billId = getIntent().getStringExtra("BIllID");
        initView();
    }

    private void initView() {
        navigationBar.setTitle("验证码");
        navigationBar.setBackground(R.color.color_4F5051);
        et_SMS = findViewById(R.id.SMS);
        b_SendSMS = findViewById(R.id.SendSMS);
        b_doneSMS = findViewById(R.id.doneSms);
        b_SendSMS.setOnClickListener(this);
        b_doneSMS.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.SendSMS:
                sendExtractSms();
                break;
            case R.id.doneSms:
                if(StringUtil.isEmpty(et_SMS.getText().toString())){
                    Toast.makeText(SendSMSActivity.this,"请输入验证码",Toast.LENGTH_LONG);
                    return;
                }
                extract();
                break;
        }
    }

    private void sendExtractSms(){
        RequestUtil requestUtil = ExtractRequestFactory.sendExtractSms(SendSMSActivity.this,billId);
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void extract(){
        RequestUtil requestUtil = ExtractRequestFactory.extract(SendSMSActivity.this,et_SMS.getText().toString(),billId);
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                SendSMSActivity.this.finish();
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }
}
