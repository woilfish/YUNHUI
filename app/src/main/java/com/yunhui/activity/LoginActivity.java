package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.bean.UserInfo;
import com.yunhui.component.edittext.ClearEditTextLogin;
import com.yunhui.component.edittext.ShowPasswordEditText;
import com.yunhui.encryption.CommonEncrypt;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.LoginRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.MobileUtil;
import com.yunhui.util.ToastUtil;

import org.apache.commons.logging.Log;
import org.json.JSONObject;

/**
 * Created by pengmin on 2018/4/9.
 * 登录页面
 */

public class LoginActivity extends BaseActionBarActivity{

    private ClearEditTextLogin clearEditTextLogin;
    private ShowPasswordEditText showPasswordEditText;
    private Button btn_LoginEnter;
    private Button btn_LoginRegist;
    private Button btn_loginForgetPassword;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始话view
     */
    private void initView(){
        hideNavigationBar();
        clearEditTextLogin = findViewById(R.id.login_phonenum);
        showPasswordEditText = findViewById(R.id.login_password);
        btn_LoginEnter = findViewById(R.id.login_enter);
        btn_LoginRegist = findViewById(R.id.login_regist);
        btn_loginForgetPassword = findViewById(R.id.login_forgetpassword);
        btn_LoginEnter.setOnClickListener(this);
        btn_LoginRegist.setOnClickListener(this);
        btn_loginForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.login_enter://登录
                if(MobileUtil.isPhoneNumber(clearEditTextLogin.getText().toString())){
                    login();
                }else{
                    ToastUtil.toast(LoginActivity.this,getResources().getString(R.string.regist_phonenum_toast));
                }
                break;
            case R.id.login_regist://注册
                Intent registIntent = new Intent(LoginActivity.this,RegistActivity.class);
//                startActivity(registIntent);
                startActivityForResult(registIntent,100);
                break;
            case R.id.login_forgetpassword://忘记密码
                Intent forgetPasswordIntent = new Intent(LoginActivity.this,RetrievePasswordActivity.class);
                startActivityForResult(forgetPasswordIntent,200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                LoginActivity.this.finish();
                break;
        }

    }

    private void login(){
        RequestUtil requestUtil = LoginRequestFactory.createLoginRequest(LoginActivity.this,clearEditTextLogin.getText().toString(),"111111", CommonEncrypt.loginEncrypt(showPasswordEditText.getText().toString()));
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                UserInfo userInfo = new UserInfo(jsonObject);
                YhApplication.getInstance().setUserInfo(userInfo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(homeIntent);
                        LoginActivity.this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
