package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.bean.MyEarnings;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.StringUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by pengmin on 2018/4/16.
 * BTC对话
 */

public class ExchangeActivity extends BaseActionBarActivity{

    private CircleImageView civ_exchangeUserPhoto;
    private TextView tv_exchangeUserPhoneNum;
    private TextView tv_exchangeHiteTop;
    private TextView tv_exchangeclouddrill;
    private TextView tv_exchangeBTC;
    private EditText et_exchangeHiteNum;
    private TextView tv_exchangePay;
    private Button b_exchangeHiteAll;
    private TextView tv_exchangeFormalities;
    private EditText et_exchangeAdd;
    private EditText et_exchangePassword;
    private Button b_exchangeCancle;
    private Button b_exchangeConfirm;
    private MyEarnings myEarnings;
    private BigDecimal bigDecimal = new BigDecimal("0.000000065");


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allTotalRevenueRequets();
    }

    private void initView() {
        navigationBar.setTitle("BTC兑换");
        navigationBar.setBackground(R.color.color_4F5051);
        civ_exchangeUserPhoto = findViewById(R.id.exchangeuserphoto);
        tv_exchangeUserPhoneNum = findViewById(R.id.exchangeuserphonenum);
        tv_exchangeUserPhoneNum.setText(YhApplication.getInstance().getUserInfo().getMobile());
        tv_exchangeHiteTop = findViewById(R.id.exchangehitetop);
        et_exchangeHiteNum = findViewById(R.id.exchangehitenum);
        tv_exchangePay = findViewById(R.id.exchangepay);
        b_exchangeHiteAll = findViewById(R.id.exchangehiteall);
        tv_exchangeFormalities = findViewById(R.id.exchangeformalities);
        et_exchangeAdd = findViewById(R.id.exchangeadd);
        et_exchangePassword = findViewById(R.id.exchangepassword);
        b_exchangeCancle = findViewById(R.id.exchangecancle);
        b_exchangeConfirm = findViewById(R.id.exchangeconfirm);
        tv_exchangeclouddrill = findViewById(R.id.exchangeclouddrill);
        tv_exchangeBTC = findViewById(R.id.exchangeBTC);

        b_exchangeHiteAll.setOnClickListener(this);
        b_exchangeCancle.setOnClickListener(this);
        b_exchangeConfirm.setOnClickListener(this);

        et_exchangeHiteNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(StringUtil.isEmpty(et_exchangeHiteNum.getText().toString())){
                    return;
                }
                final BigDecimal bigDecimalNum = new BigDecimal(et_exchangeHiteNum.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_exchangePay.setText("共兑换 " + bigDecimal.multiply(bigDecimalNum).toPlainString() + " BTC");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.exchangehiteall://全部兑换
                if(myEarnings != null) {
                    et_exchangeHiteNum.setText(myEarnings.getTotal());
                }else{
                    ToastUtil.toast(ExchangeActivity.this,"查询个人收益失败");
                }
                break;
            case R.id.exchangecancle://取消
                break;
            case R.id.exchangeconfirm://确认兑换
                if(StringUtil.isEmpty(et_exchangeHiteNum.getText().toString())){
                    ToastUtil.toast(ExchangeActivity.this,"请输入数量");
                    return;
                }
                if(Integer.parseInt(et_exchangeHiteNum.getText().toString()) < 1000){
                    ToastUtil.toast(ExchangeActivity.this,"兑换BTC最低云钻数量1000");
                    return;
                }
                if(Integer.parseInt(et_exchangeHiteNum.getText().toString()) > Integer.parseInt(myEarnings.getTotal())){
                    ToastUtil.toast(ExchangeActivity.this,"您输入的云钻数量已超出了您的全部云钻,请重新输入");
                    return;
                }
                exchange();
                break;
        }
    }

    private void exchange(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(ExchangeActivity.this,"user/cloundToBtcoin", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("cloundCoin",et_exchangeHiteNum.getText().toString());
        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
//                Intent intent = new Intent(ExchangeActivity.this,ExchangeResultActivity.class);
//                startActivity(intent);
//                finish();
                ToastUtil.toast(ExchangeActivity.this,"兑换BTC成功");
                allTotalRevenueRequets();
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
                ToastUtil.toast(ExchangeActivity.this,"兑换BTC失败");
            }
        });
        requestUtil.execute();
    }

    private void allTotalRevenueRequets(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(ExchangeActivity.this,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject != null && jsonObject.has("total") && jsonObject.has("btcoin")){
                    myEarnings = new MyEarnings(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_exchangeclouddrill.setText("云钻:" + myEarnings.getTotal());
                            tv_exchangeBTC.setText("BTC:" + myEarnings.getBtcoin());
                        }
                    });
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
