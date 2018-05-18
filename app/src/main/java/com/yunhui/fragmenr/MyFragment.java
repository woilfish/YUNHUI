package com.yunhui.fragmenr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.YhApplication;
import com.yunhui.activity.AboutAppActivity;
import com.yunhui.activity.BuyillMActivity;
import com.yunhui.activity.ExchangeActivity;
import com.yunhui.activity.ExtractActivity;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.activity.InviteCodeActivity;
import com.yunhui.bean.MyEarnings;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.component.linearlayout.LabelItemView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;

import org.json.JSONObject;

/**
 * Created by pengmin on 2018/4/2.
 * 我的
 */

public class MyFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private View parentView;
    private CircleImageView civ_userPhoto;
    private TextView tv_userPhoneNum;
    private LabelItemView liv_exchange;
    private LabelItemView liv_myTask;
    private LabelItemView liv_buy;
    private LabelItemView liv_myInvitation;
    private LabelItemView liv_aboutMy;
    private LabelItemView liv_extract;
    private TextView tv_myData;
    private TextView tv_cloudDrill;
    private TextView tv_BTC;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_my,null);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        allTotalRevenueRequets();
    }

    private void initView() {
        civ_userPhoto = parentView.findViewById(R.id.userphoto);
        tv_userPhoneNum = parentView.findViewById(R.id.userphonenum);
        tv_userPhoneNum.setText(YhApplication.getInstance().getUserInfo().getMobile());
        liv_exchange = parentView.findViewById(R.id.exchange);
        liv_myTask = parentView.findViewById(R.id.myTask);
        liv_buy = parentView.findViewById(R.id.buy);
        liv_myInvitation = parentView.findViewById(R.id.myInvitation);
        liv_aboutMy = parentView.findViewById(R.id.aboutMy);
        tv_myData = parentView.findViewById(R.id.mydate);
        liv_extract = parentView.findViewById(R.id.extract);
        tv_cloudDrill = parentView.findViewById(R.id.clouddrill);
        tv_BTC = parentView.findViewById(R.id.BTC);
        tv_myData.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        liv_exchange.setOnClickListener(this);
        liv_myTask.setOnClickListener(this);
        liv_buy.setOnClickListener(this);
        liv_myInvitation.setOnClickListener(this);
        liv_aboutMy.setOnClickListener(this);
        liv_extract.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.exchange:
                Intent exchangeIntent = new Intent(homeActivity, ExchangeActivity.class);
                startActivity(exchangeIntent);
                break;
            case R.id.extract:
                Intent extractIntent = new Intent(homeActivity, ExtractActivity.class);
                startActivity(extractIntent);
                break;
            case R.id.myTask:
                break;
            case R.id.buy:
                Intent buyIntent = new Intent(homeActivity, BuyillMActivity.class);
                startActivityForResult(buyIntent,7777);
                break;
            case R.id.myInvitation:
                Intent myInvitationIntent = new Intent(homeActivity, InviteCodeActivity.class);
                startActivity(myInvitationIntent);
                break;
            case R.id.aboutMy:
                Intent aboutIntent = new Intent(homeActivity, AboutAppActivity.class);
                startActivity(aboutIntent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 7777:
                homeActivity.toTabItem(2,null);
                break;
        }
    }

    private void allTotalRevenueRequets(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(homeActivity,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject != null && jsonObject.has("total") && jsonObject.has("btcoin")){
                    final MyEarnings myEarnings = new MyEarnings(jsonObject);
                    homeActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_cloudDrill.setText("云钻:" + myEarnings.getTotal());
                            tv_BTC.setText("BTC:" + myEarnings.getBtcoin());
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
