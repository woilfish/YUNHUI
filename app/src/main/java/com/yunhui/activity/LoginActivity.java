package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yunhui.R;
import com.yunhui.component.edittext.ClearEditTextLogin;
import com.yunhui.component.edittext.ShowPasswordEditText;

import org.apache.commons.logging.Log;

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
                break;
            case R.id.login_regist://注册
                Intent registIntent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(registIntent);
                break;
            case R.id.login_forgetpassword://忘记密码
                break;
        }
    }
}
