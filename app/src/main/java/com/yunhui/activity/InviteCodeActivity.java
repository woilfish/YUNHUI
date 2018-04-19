package com.yunhui.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhui.R;

/**
 * Created by pengmin on 2018/4/19.
 * 邀请码
 */

public class InviteCodeActivity extends BaseActionBarActivity{

    private TextView tv_inviteSuccessCode;
    private TextView tv_inviteCode;
    private ImageView im_inviteQrCode;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite_code);
        initView();

//        SpannableString spanableInfo = new SpannableString("今日收款 " + count + " 笔 ");
//        spanableInfo.setSpan(new Clickable(), 5, count.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        liv_homeViewPageerFirstTitle.setLabelText(spanableInfo);
    }

    private void initView() {
        tv_inviteSuccessCode = findViewById(R.id.inviteSuccessCode);
        tv_inviteCode = findViewById(R.id.invitecode);
        im_inviteQrCode = findViewById(R.id.inviteqrcode);
    }
}
