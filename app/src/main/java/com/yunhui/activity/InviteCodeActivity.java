package com.yunhui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.request.RequestUtil;

import org.json.JSONObject;

/**
 * Created by pengmin on 2018/4/19.
 * 邀请码
 */

public class InviteCodeActivity extends BaseActionBarActivity{

    private TextView tv_inviteSuccessCode;
    private TextView tv_inviteCode;
    private ImageView im_inviteQrCode;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 3:
                    String count = (String) msg.obj;
                    SpannableString spanableInfo = new SpannableString("已成功邀请 " + count + " 个 ");
                    spanableInfo.setSpan(null, 5, count.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_inviteSuccessCode.setText(spanableInfo);
                    break;
            }
        }
    };

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite_code);
        initView();


    }

    private void initView() {
        navigationBar.setTitle("我的邀请码");
        navigationBar.setBackground(R.color.color_4F5051);
        tv_inviteSuccessCode = findViewById(R.id.inviteSuccessCode);
        tv_inviteCode = findViewById(R.id.invitecode);
        im_inviteQrCode = findViewById(R.id.inviteqrcode);
    }

    private void queryUserRecommendCount(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(InviteCodeActivity.this,"user/queryUserRecommendCount", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("count")){
                    String count = jsonObject.optString("count");
                    Message messags = new Message();
                    messags.what = 3;
                    messags.obj = count;
                    handler.sendMessage(messags);
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });

        requestUtil.execute();
    }

}
