package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunhui.R;

/**
 * Created by pengmin on 2018/4/15.
 * 找回密码
 */

public class RetrievePasswordActivity extends BaseActionBarActivity{

    private EditText et_retrievepasswordphonenum;
    private Button b_retrievepasswordnext;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_retrieve_password);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        et_retrievepasswordphonenum = findViewById(R.id.retrievepasswordphonenum);
        b_retrievepasswordnext = findViewById(R.id.retrievepasswordnext);
        b_retrievepasswordnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
