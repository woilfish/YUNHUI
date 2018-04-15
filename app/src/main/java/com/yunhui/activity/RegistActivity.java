package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunhui.R;

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


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regist);
        hideNavigationBar();
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.registsendvalidation://发送验证码
                break;
            case R.id.registenter://注册
                break;
            case R.id.registloginenter://登录
                break;
        }
    }
}
