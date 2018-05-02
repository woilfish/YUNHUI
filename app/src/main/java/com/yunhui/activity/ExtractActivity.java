package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunhui.R;

/**
 * Created by pengmin on 2018/5/1.
 */

public class ExtractActivity extends BaseActionBarActivity{

    private EditText et_extractNum;
    private Button b_extractAllNum;
    private EditText et_extractWallet;
    private EditText et_extractSMS;
    private Button b_extractSendSMS;
    private Button b_extractCancle;
    private Button b_extractConfirm;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_extract);
        initView();
    }

    private void initView() {
        navigationBar.setTitle("BTC提取");
        navigationBar.setBackground(R.color.color_4F5051);
        et_extractNum = findViewById(R.id.extractnum);
        b_extractAllNum = findViewById(R.id.extractAllNum);
        et_extractWallet = findViewById(R.id.extractwallet);
        et_extractSMS = findViewById(R.id.extractSMS);
        b_extractSendSMS = findViewById(R.id.extractSendSMS);
        b_extractCancle = findViewById(R.id.extractcancle);
        b_extractConfirm = findViewById(R.id.extractconfirm);
        b_extractAllNum.setOnClickListener(this);
        b_extractSendSMS.setOnClickListener(this);
        b_extractCancle.setOnClickListener(this);
        b_extractConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.extractAllNum:
                break;
            case R.id.extractSendSMS:
                break;
            case R.id.extractcancle:
                break;
            case R.id.extractconfirm:
                break;
        }
    }
}
