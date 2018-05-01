package com.yunhui.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResultType;
import com.lakala.zxing.scanner.encode.QREncode;
import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
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
        generateQrCode();
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

    private void generateQrCode(){
        try {

            String qrCode = YhApplication.getInstance().getUserInfo().getUserId();
            int type = 4;

            ParsedResultType parsedResultType = null;
            switch (type) {
                case 0:
                    parsedResultType = ParsedResultType.ADDRESSBOOK;
                    break;
                case 1:
                    parsedResultType = ParsedResultType.EMAIL_ADDRESS;
                    break;
                case 2:
                    parsedResultType = ParsedResultType.PRODUCT;
                    break;
                case 3:
                    parsedResultType = ParsedResultType.URI;
                    break;
                case 4:
                    parsedResultType = ParsedResultType.TEXT;
                    break;
                case 5:
                    parsedResultType = ParsedResultType.GEO;
                    break;
                case 6:
                    parsedResultType = ParsedResultType.TEL;
                    break;
                case 7:
                    parsedResultType = ParsedResultType.SMS;
                    break;
                case 8:
                    parsedResultType = ParsedResultType.CALENDAR;
                    break;
                case 9:
                    parsedResultType = ParsedResultType.WIFI;
                    break;
                case 10:
                    parsedResultType = ParsedResultType.ISBN;
                    break;
                case 11:
                    parsedResultType = ParsedResultType.VIN;
                    break;
            }
            Resources res = this.getResources();
//            Bitmap logoBitmap = BitmapFactory.decodeResource(res, R.drawable.plat_app_icon);//二维码中的logo
            Bitmap qrBg = BitmapFactory.decodeResource(res, R.drawable.bg_corner_light);//二维码背景
            Bitmap bitmap = new QREncode.Builder(this)
                    .setColor(res.getColor(R.color.black))//二维码颜色
                    .setQrBackground(qrBg)//二维码背景
                    .setMargin(0)//二维码边框
                    .setParsedResultType(parsedResultType)//二维码类型
                    .setContents(qrCode)//二维码内容
                    .setSize(500)//二维码等比大小
//                    .setLogoBitmap(logoBitmap,40)//二维码中间logo大小
                    .build().encodeAsBitmap();
            im_inviteQrCode.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
