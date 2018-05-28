package com.yunhui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.YhApplication;
import com.yunhui.adapter.RechargeAdapter;
import com.yunhui.adapter.TaskAdapter;
import com.yunhui.bean.MyEarnings;
import com.yunhui.bean.RechargeBean;
import com.yunhui.clickinterface.ListItemClickHelp;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.BuyRequestFactory;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 充值页面
 */
public class RechargeActivity extends BaseActionBarActivity implements RefreshListView.OnRefreshListViewListener,ListItemClickHelp{

    private MyEarnings myEarnings;
    private CircleImageView cim_rechargeUserPhoto;
    private TextView tv_rechargeUserPhoneNum;
    private TextView tv_rechargeCloudDrill;
    private TextView tv_rechargeBTC;
    private List<RechargeBean> rechargeBeans;
    private RefreshListView rlv_rechargeList;
    private RechargeAdapter rechargeAdapter;
    private TextView tv_rechargeWallet;
    private Button b_rechargepay;
    private int c1 = 0;
    private int c2 = 0;
    private int c3 = 0;
    private double allTotal = 0.00;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0000:
                    rechargeAdapter.refreshData(rechargeBeans);
                    break;
            }
        }
    };

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        initView();
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    private void initView() {
        navigationBar.setTitle("云钻充值");
        navigationBar.setBackground(R.color.color_4F5051);
        cim_rechargeUserPhoto = findViewById(R.id.rechargeuserphoto);
        tv_rechargeUserPhoneNum = findViewById(R.id.rechargeuserphonenum);
        tv_rechargeUserPhoneNum.setText(YhApplication.getInstance().getUserInfo().getMobile());
        tv_rechargeWallet = findViewById(R.id.rechargeWallet);
        b_rechargepay = findViewById(R.id.rechargepay);
        tv_rechargeCloudDrill = findViewById(R.id.rechargeclouddrill);
        tv_rechargeBTC = findViewById(R.id.rechargeBTC);
        rlv_rechargeList = findViewById(R.id.rechargeList);
        rechargeAdapter = new RechargeAdapter(RechargeActivity.this,rechargeBeans,this);
        rlv_rechargeList.setAdapter(rechargeAdapter);
        rlv_rechargeList.setOnRefreshListViewListener(this);
        rlv_rechargeList.setPullRefreshEnable(false);
        rlv_rechargeList.setPullLoadEnable(false);
        b_rechargepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayBill();
            }
        });

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View item, View parent, int position, int which) {

        TextView tv_num = item.findViewById(R.id.rechargeNum);

        switch (position){
            case 0:
                switch (which){
                    case R.id.rechargeReduction:

                        if(c1 != 0){
                            c1 --;
                            tv_num.setText(String.valueOf(c1));
                            allTotal = allTotal - rechargeBeans.get(position).getAmount();
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }
                        break;
                    case R.id.rechargeAdd:
                        if(c1 != 10){
                            c1 ++;
                            tv_num.setText(String.valueOf(c1));
                            allTotal = allTotal + rechargeBeans.get(position).getAmount() ;
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }else{
                            ToastUtil.toast(RechargeActivity.this,"购买数量已达今日单笔");
                        }
                        break;
                }
                break;
            case 1:
                switch (which){
                    case R.id.rechargeReduction:

                        if(c2 != 0){
                            c2 --;
                            tv_num.setText(String.valueOf(c2));
                            allTotal = allTotal - rechargeBeans.get(position).getAmount();
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }
                        break;
                    case R.id.rechargeAdd:
                        if(c2 != 10){
                            c2 ++;
                            tv_num.setText(String.valueOf(c2));
                            allTotal = allTotal + rechargeBeans.get(position).getAmount();
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }else{
                            ToastUtil.toast(RechargeActivity.this,"购买数量已达今日单笔");
                        }
                        break;
                }
                break;
            case 2:
                switch (which){
                    case R.id.rechargeReduction:

                        if(c3 != 0){
                            c3 --;
                            tv_num.setText(String.valueOf(c3));
                            allTotal = allTotal - rechargeBeans.get(position).getAmount();
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }
                        break;
                    case R.id.rechargeAdd:
                        if(c3 != 10){
                            c3 ++;
                            tv_num.setText(String.valueOf(c3));
                            allTotal = allTotal + rechargeBeans.get(position).getAmount();
                            tv_rechargeWallet.setText("合计:￥" + String.format("%.2f",allTotal));
                        }else{
                            ToastUtil.toast(RechargeActivity.this,"购买数量已达今日单笔");
                        }
                        break;
                }
                break;
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allTotalRevenueRequets();
        queryChargeList();
    }

    private void allTotalRevenueRequets(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(RechargeActivity.this,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

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
                            tv_rechargeCloudDrill.setText("云钻:" + myEarnings.getTotal());
                            tv_rechargeBTC.setText("BTC:" + myEarnings.getBtcoin());
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

    private void queryChargeList(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(RechargeActivity.this,"user/queryChargeList", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("list")){
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    RechargeBean rechargeBean = new RechargeBean(jsonArray);
                    rechargeBeans = rechargeBean.getRechargeBeans();
                    Message message = new Message();
                    message.what = 0000;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void createPayBill(){

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


}
