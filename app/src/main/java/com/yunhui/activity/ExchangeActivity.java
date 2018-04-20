package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.component.image.CircleImageView;

/**
 * Created by pengmin on 2018/4/16.
 * BTC对话
 */

public class ExchangeActivity extends BaseActionBarActivity{

    private CircleImageView civ_exchangeUserPhoto;
    private TextView tv_exchangeUserPhoneNum;
    private TextView tv_exchangeUserName;
    private TextView tv_exchangeHiteTop;
    private EditText et_exchangeHiteNum;
    private TextView tv_exchangePay;
    private Button b_exchangeHiteAll;
    private TextView tv_exchangeFormalities;
    private EditText et_exchangeAdd;
    private EditText et_exchangePassword;
    private Button b_exchangeCancle;
    private Button b_exchangeConfirm;


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange);
        initView();
    }

    private void initView() {
        navigationBar.setTitle("BTC兑换");
        civ_exchangeUserPhoto = findViewById(R.id.exchangeuserphoto);
        tv_exchangeUserPhoneNum = findViewById(R.id.exchangeuserphonenum);
        tv_exchangeUserName = findViewById(R.id.exchangeusername);
        tv_exchangeHiteTop = findViewById(R.id.exchangehitetop);
        et_exchangeHiteNum = findViewById(R.id.exchangehitenum);
        tv_exchangePay = findViewById(R.id.exchangepay);
        b_exchangeHiteAll = findViewById(R.id.exchangehiteall);
        tv_exchangeFormalities = findViewById(R.id.exchangeformalities);
        et_exchangeAdd = findViewById(R.id.exchangeadd);
        et_exchangePassword = findViewById(R.id.exchangepassword);
        b_exchangeCancle = findViewById(R.id.exchangecancle);
        b_exchangeConfirm = findViewById(R.id.exchangeconfirm);

        b_exchangeHiteAll.setOnClickListener(this);
        b_exchangeCancle.setOnClickListener(this);
        b_exchangeConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.exchangehiteall://全部兑换
                break;
            case R.id.exchangecancle://取消
                break;
            case R.id.exchangeconfirm://确认兑换
                break;
        }
    }
}
