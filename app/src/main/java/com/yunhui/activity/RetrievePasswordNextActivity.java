package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.encryption.CommonEncrypt;
import com.yunhui.request.RegistRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.request.RetrievePasswordFactory;
import com.yunhui.util.StringUtil;
import com.yunhui.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pengmin on 2018/4/15.
 * 找回密码下一步
 */

public class RetrievePasswordNextActivity extends BaseActionBarActivity {

    private EditText et_retrievepasswordvalidation;
    private EditText et_retrievepasswordpassword;
    private EditText et_retrievepasswordconfirmpassword;
    private Button b_retrievepasswordsendvalidation;
    private Button b_retrievepassworddone;
    private String mobile;
    private int smsTime = 60;
    private Timer timer;


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_retrieve_password_next);
        initView();
        mobile = getIntent().getStringExtra("Mobile");
    }

    /**
     * 初始化View
     */
    private void initView() {
        navigationBar.setTitle("找回密码");
        navigationBar.setBackground(R.color.color_4F5051);
        et_retrievepasswordvalidation = findViewById(R.id.retrievepasswordvalidation);
        et_retrievepasswordpassword = findViewById(R.id.retrievepasswordpassword);
        et_retrievepasswordconfirmpassword = findViewById(R.id.retrievepasswordconfirmpassword);
        b_retrievepasswordsendvalidation = findViewById(R.id.retrievepasswordsendvalidation);
        b_retrievepassworddone = findViewById(R.id.retrievepassworddone);
        b_retrievepasswordsendvalidation.setOnClickListener(this);
        b_retrievepassworddone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.retrievepasswordsendvalidation:
                startTimer();
                sendSms();
                b_retrievepasswordsendvalidation.setEnabled(false);
                break;
            case R.id.retrievepassworddone:
                if(StringUtil.isEmpty(et_retrievepasswordvalidation.getText().toString())){
                    Toast.makeText(RetrievePasswordNextActivity.this,"请输入验证码",Toast.LENGTH_LONG);
                    return;
                }
                if(et_retrievepasswordpassword.getText().toString().equals(et_retrievepasswordconfirmpassword.getText().toString()) &&
                        StringUtil.isNotEmpty(et_retrievepasswordpassword.getText().toString()) && StringUtil.isNotEmpty(et_retrievepasswordconfirmpassword.getText().toString())){
                    updataPwd();
                }else{
                    Toast.makeText(RetrievePasswordNextActivity.this,"请输入新密码",Toast.LENGTH_LONG);
                    return;
                }

                break;
        }
    }

    private void sendSms(){

        RequestUtil requestUtil = RetrievePasswordFactory.sendNoSms(RetrievePasswordNextActivity.this,mobile);
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

    private void updataPwd(){
        RequestUtil requestUtil = RetrievePasswordFactory.updataPwd(RetrievePasswordNextActivity.this,mobile,et_retrievepasswordvalidation.getText().toString(), CommonEncrypt.loginEncrypt(et_retrievepasswordpassword.getText().toString()),CommonEncrypt.loginEncrypt(et_retrievepasswordconfirmpassword.getText().toString()));
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                ToastUtil.toast(RetrievePasswordNextActivity.this,"密码修改成功,请重新登录");
                RetrievePasswordNextActivity.this.setResult(RESULT_OK);
                RetrievePasswordNextActivity.this.finish();
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
                ToastUtil.toast(RetrievePasswordNextActivity.this,"密码修改失败");
                RetrievePasswordNextActivity.this.setResult(RESULT_OK);
                RetrievePasswordNextActivity.this.finish();
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
                            b_retrievepasswordsendvalidation.setText("重新发送");
                            smsTime = 60;
                            timer.cancel();
                            b_retrievepasswordsendvalidation.setEnabled(true);
                            return;
                        }
                        smsTime --;
                        b_retrievepasswordsendvalidation.setText(String.valueOf(smsTime));
                    }
                });

            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
