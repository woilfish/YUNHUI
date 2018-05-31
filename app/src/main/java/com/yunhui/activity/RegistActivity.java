package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.client.result.ParsedResultType;
import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.DefaultHttpResponseHandler;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.bean.RequestRegistBean;
import com.yunhui.bean.UserInfo;
import com.yunhui.component.dialog.AlertDialog;
import com.yunhui.encryption.CommonEncrypt;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RegistRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.MobileUtil;
import com.yunhui.util.ToastUtil;
import com.yunhui.zxscan.ScannerActivity;
import com.yunhui.zxscan.ScannerEnum;

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
    private Button b_scanner;
    private int smsTime = 60;
    private Timer timer;
    private AlertDialog alertDialog;


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regist);
        hideNavigationBar();
        initView();
        listenEvent();
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
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
        b_scanner = findViewById(R.id.scanner);
        b_registenter.setOnClickListener(this);
        b_registloginenter.setOnClickListener(this);
        b_registsendvalidation.setOnClickListener(this);
        b_scanner.setOnClickListener(this);
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
            case R.id.scanner:
                Intent intent1 = new Intent(RegistActivity.this, ScannerActivity.class);
                startActivityForResult(intent1,1234);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1234:
                    Bundle bundle = data.getExtras();
                    if(ScannerEnum.TEXT == bundle.getSerializable("type")){
                        et_registinvitecode.setText(bundle.getString("SNCode"));
                    }

                break;
        }
    }

    private void registEnter(){

        RequestRegistBean requestRegistBean = new RequestRegistBean();
        requestRegistBean.setMobile(et_registphonenum.getText().toString());
        requestRegistBean.setCode(et_registvalidation.getText().toString());
        requestRegistBean.setPassword(CommonEncrypt.loginEncrypt(et_registpassword.getText().toString()));
        requestRegistBean.setConfirmPassword(CommonEncrypt.loginEncrypt(et_registconfirmpassword.getText().toString()));
        requestRegistBean.setInviteCode(et_registinvitecode.getText().toString());
        RequestUtil requestUtil = RegistRequestFactory.createRegistRequest(RegistActivity.this,requestRegistBean);
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);

                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                UserInfo userInfo = new UserInfo(jsonObject);
                YhApplication.getInstance().setUserInfo(userInfo);
                showAlertDialog();
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

    private void showAlertDialog(){
        alertDialog = new AlertDialog();
        alertDialog.setButtons("登录");
        alertDialog.setButtonsTextColor(R.color.color_EE9707);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("恭喜您，注册成功！");
        alertDialog.setDialogDelegate(new AlertDialog.AlertDialogDelegate(){
            @Override
            public void onButtonClick(AlertDialog dialog, View view, int index) {
                super.onButtonClick(dialog, view, index);
                switch (index){
                    case 0:
                        alertDialog.dismiss();
                        Intent intent = new Intent(RegistActivity.this,HomeActivity.class);
                        startActivity(intent);
                        RegistActivity.this.setResult(RESULT_OK);
                        RegistActivity.this.finish();
                        break;
                }
            }
        });
        alertDialog.show(RegistActivity.this.getSupportFragmentManager());
    }
}
