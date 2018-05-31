package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.StringUtil;
import com.yunhui.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

public class GuessSendSMSActivity extends BaseActionBarActivity{

    private EditText et_SMS;
    private Button b_SendSMS;
    private Button b_doneSMS;
    private String billId;
    private int smsTime = 60;
    private Timer timer;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_sms);
        billId = getIntent().getStringExtra("billid");
        initView();
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
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
                startTimer();
                sendExtractSms();
                b_SendSMS.setEnabled(false);
                break;
            case R.id.doneSms:
                if(StringUtil.isEmpty(et_SMS.getText().toString())){
                    Toast.makeText(GuessSendSMSActivity.this,"请输入验证码",Toast.LENGTH_LONG);
                    return;
                }
                extract();
                break;
        }
    }

    private void sendExtractSms(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(GuessSendSMSActivity.this,"user/sendGuessSms", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("billId",billId);

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
        RequestUtil requestUtil = RequestUtil.obtainRequest(GuessSendSMSActivity.this,"user/guessPay", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("billId",billId);
        httpRequestParams.put("Code",et_SMS.getText().toString());
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                ToastUtil.toast(GuessSendSMSActivity.this,"投注成功");
                setResult(RESULT_OK);
                GuessSendSMSActivity.this.finish();
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void startTimer(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(smsTime == 1){
                            b_SendSMS.setText("重新发送");
                            smsTime = 60;
                            timer.cancel();
                            b_SendSMS.setEnabled(true);
                            return;
                        }
                        smsTime --;
                        b_SendSMS.setText(String.valueOf(smsTime));
                    }
                });

            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
