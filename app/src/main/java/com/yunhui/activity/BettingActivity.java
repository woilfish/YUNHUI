package com.yunhui.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.adapter.GuessAdapter;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.MyEarnings;
import com.yunhui.clickinterface.ListItemClickHelp;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.util.List;

public class BettingActivity extends BaseActionBarActivity implements View.OnClickListener,ListItemClickHelp,RefreshListView.OnRefreshListViewListener{

    private CircleImageView cim_bettingUserPhoto;
    private TextView tv_bettingUserPhoneNum;
    private TextView tv_bettingCloudDrill;
    private TextView tv_bettingBTC;
    private MyEarnings myEarnings;
    private RefreshListView rlv_bettingList;
    private TextView tv_betting;
    private Button b_bettingReduction;
    private Button b_bettingAdd;
    private TextView tv_bettingNum;
    private TextView tv_allMoney;
    private Button b_bettingOk;
    private GuessAdapter guessAdapter;
    private List<GuessListBean> guessListBeans;
    private int count = 1;
    private int buyNum = 0;
    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_betting);
        ActivityQueueManager.getInstance().pushActivity(this);
        guessListBeans = (List<GuessListBean>) getIntent().getSerializableExtra("list");
        initView();
    }

    private void initView(){
        navigationBar.setTitle("投注");
        navigationBar.setBackground(R.color.color_4F5051);
        cim_bettingUserPhoto = findViewById(R.id.bettingeuserphoto);
        tv_bettingUserPhoneNum = findViewById(R.id.bettiinguserphonenum);
        tv_bettingUserPhoneNum.setText(YhApplication.getInstance().getUserInfo().getMobile());
        tv_bettingCloudDrill = findViewById(R.id.bettingclouddrill);
        tv_bettingBTC = findViewById(R.id.bettingBTC);
        rlv_bettingList = findViewById(R.id.bettingList);
        tv_betting = findViewById(R.id.betting);
        b_bettingReduction = findViewById(R.id.bettingReduction);
        b_bettingAdd = findViewById(R.id.bettingAdd);
        tv_bettingNum = findViewById(R.id.bettingNum);
        tv_allMoney = findViewById(R.id.allMoney);
        b_bettingOk = findViewById(R.id.bettingOk);
        b_bettingReduction.setOnClickListener(this);
        b_bettingAdd.setOnClickListener(this);
        b_bettingOk.setOnClickListener(this);
        guessAdapter = new GuessAdapter(BettingActivity.this,guessListBeans,this);
        rlv_bettingList.setAdapter(guessAdapter);

        rlv_bettingList.setOnRefreshListViewListener(this);
        rlv_bettingList.setPullRefreshEnable(false);
        rlv_bettingList.setPullLoadEnable(false);

        SpannableString spanableInfo = new SpannableString("共 " + count + " 注 ");
        spanableInfo.setSpan(null, 1, String.valueOf(count).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_betting.setText(spanableInfo);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.bettingReduction:
                if(buyNum != 0){
                    buyNum --;
                    tv_bettingNum.setText(String.valueOf(buyNum));
                    showAllMoney(buyNum);
                }
                break;
            case R.id.bettingAdd:
                if(buyNum != 10){
                    buyNum ++;
                    tv_bettingNum.setText(String.valueOf(buyNum));
                    showAllMoney(buyNum);
                }else{
                    ToastUtil.toast(BettingActivity.this,"已达当前下注的最大购买");
                }
                break;
            case R.id.bettingOk:
                break;
        }
    }

    public void showAllMoney(int buyNum){
        SpannableString spanableInfo = new SpannableString("合计 " + buyNum * 100 + " 云钻 ");
        spanableInfo.setSpan(null, 2, String.valueOf(buyNum * 100).length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_allMoney.setText(spanableInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allTotalRevenueRequets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }



    private void allTotalRevenueRequets(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(BettingActivity.this,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

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
                            tv_bettingCloudDrill.setText("云钻:" + myEarnings.getTotal());
                            tv_bettingBTC.setText("BTC:" + myEarnings.getBtcoin());
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

    @Override
    public void onClick(View item, View parent, int position, int which) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
