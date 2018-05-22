package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunhui.R;
import com.yunhui.util.MobileUtil;
import com.yunhui.util.ToastUtil;

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
        navigationBar.setTitle("找回密码");
        navigationBar.setBackground(R.color.color_4F5051);
        et_retrievepasswordphonenum = findViewById(R.id.retrievepasswordphonenum);
        b_retrievepasswordnext = findViewById(R.id.retrievepasswordnext);
        b_retrievepasswordnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(MobileUtil.isPhoneNumber(et_retrievepasswordphonenum.getText().toString())){
            Intent nextIntent = new Intent(RetrievePasswordActivity.this,RetrievePasswordNextActivity.class);
            nextIntent.putExtra("Mobile",et_retrievepasswordphonenum.getText().toString());
            startActivityForResult(nextIntent,111);
        }else{
            ToastUtil.toast(RetrievePasswordActivity.this,getString(R.string.regist_phonenum_toast));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RetrievePasswordActivity.this.setResult(RESULT_OK);
        RetrievePasswordActivity.this.finish();
    }
}
