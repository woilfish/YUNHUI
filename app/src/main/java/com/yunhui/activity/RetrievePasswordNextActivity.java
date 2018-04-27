package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunhui.R;

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


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_retrieve_password_next);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
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
    }
}
