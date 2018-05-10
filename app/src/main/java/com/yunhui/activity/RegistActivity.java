package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.DefaultHttpResponseHandler;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.bean.RequestRegistBean;
import com.yunhui.bean.UserInfo;
import com.yunhui.encryption.CommonEncrypt;
import com.yunhui.request.RegistRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.MobileUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pengmin on 2018/4/11.
 * 注册页面
 */

public class RegistActivity extends BaseActionBarActivity{

    private EditText et_registphonenum;
    private EditText et_registvalidation;
    private EditText et_registpassword;
    private EditText et_registconfirmpassword;
    private EditText et_registinvitecode;
    private Button b_registsendvalidation;
    private Button b_registenter;
    private Button b_registloginenter;
    private int smsTime = 60;
    private Timer timer;


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regist);
        hideNavigationBar();
        initView();
        listenEvent();
    }

    /**
     * 初始化view
     */
    private void initView() {
        navigationBar.setTitle("注册");
        navigationBar.setBackground(R.color.color_4F5051);
        et_registphonenum = findViewById(R.id.registphonenum);
        et_registvalidation = findViewById(R.id.registvalidation);
        et_registpassword = findViewById(R.id.registpassword);
        et_registconfirmpassword = findViewById(R.id.registconfirmpassword);
        et_registinvitecode = findViewById(R.id.registinvitecode);
        b_registsendvalidation = findViewById(R.id.registsendvalidation);
        b_registenter = findViewById(R.id.registenter);
        b_registloginenter = findViewById(R.id.registloginenter);
        b_registenter.setOnClickListener(this);
        b_registloginenter.setOnClickListener(this);
        b_registsendvalidation.setOnClickListener(this);
    }

    /**
     * 监听事件
     */
    private void listenEvent(){
        et_registphonenum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!MobileUtil.isPhoneNumber(et_registphonenum.getText().toString())){
                        ToastUtil.toast(RegistActivity.this,getResources().getString(R.string.regist_phonenum_toast));
                    }
                }
            }
        });

        et_registvalidation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(et_registvalidation.getText().toString().length() < 6 || et_registvalidation.getText().toString().length() > 6){
                        ToastUtil.toast(RegistActivity.this,getResources().getString(R.string.regist_validation_toast));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.registsendvalidation://发送验证码
                if(MobileUtil.isPhoneNumber(et_registphonenum.getText().toString())){
                    startTimer();
                    sendSms();
                    b_registsendvalidation.setEnabled(false);
                }else{
                    ToastUtil.toast(RegistActivity.this,getResources().getString(R.string.regist_phonenum_toast));
                }
                break;
            case R.id.registenter://注册
                if(MobileUtil.isPhoneNumber(et_registphonenum.getText().toString()) && et_registvalidation.getText().toString().length() == 6) {
                    registEnter();
                }
                break;
            case R.id.registloginenter://登录
                Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
                startActivity(intent);
                RegistActivity.this.finish();
                break;
        }
    }

    private void registEnter(){

        RequestRegistBean requestRegistBean = new RequestRegistBean();
        requestRegistBean.setMobile(et_registphonenum.getText().toString());
        requestRegistBean.setCode(et_registvalidation.getText().toString());
        requestRegistBean.setPassword(CommonEncrypt.loginEncrypt(et_registpassword.getText().toString()));
        requestRegistBean.setConfirmPassword(CommonEncrypt.loginEncrypt(et_registconfirmpassword.getText().toString()));
        RequestUtil requestUtil = RegistRequestFactory.createRegistRequest(RegistActivity.this,requestRegistBean);
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);

                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                UserInfo userInfo = new UserInfo(jsonObject);
                YhApplication.getInstance().setUserInfo(userInfo);
                Intent intent = new Intent(RegistActivity.this,HomeActivity.class);
                startActivity(intent);
                RegistActivity.this.setResult(RESULT_OK);
                RegistActivity.this.finish();
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();

    }

    private void sendSms(){
        RequestUtil requestUtil = RegistRequestFactory.sendSms(RegistActivity.this,et_registphonenum.getText().toString());
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

    private void startTimer(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(smsTime == 1){
                            b_registsendvalidation.setText("重新发送");
                            smsTime = 60;
                            timer.cancel();
                            b_registsendvalidation.setEnabled(true);
                            return;
                        }
                        smsTime --;
                        b_registsendvalidation.setText(String.valueOf(smsTime));
                    }
                });

            }
        };
        timer.schedule(timerTask,0,1000);
    }

}
