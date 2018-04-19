package com.yunhui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.adapter.ProductMachineAdapter;
import com.yunhui.bean.ProductMachine;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by pengmin on 2018/4/19.
 * 购买矿机
 */

public class BuyillMActivity extends BaseActionBarActivity implements RefreshListView.OnRefreshListViewListener{

    private RefreshListView rlv_BuyRef;
    private List<ProductMachine> productMachines;
    private ProductMachineAdapter productMachineAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    productMachineAdapter.refreshData(productMachines);
                    break;
            }
        }
    };


    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buymill);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        productMachineRequest();
    }

    private void initView() {
        rlv_BuyRef = findViewById(R.id.buyref);
        productMachineAdapter = new ProductMachineAdapter(BuyillMActivity.this,productMachines);
        rlv_BuyRef.setAdapter(productMachineAdapter);
        rlv_BuyRef.setOnRefreshListViewListener(this);
        rlv_BuyRef.setPullRefreshEnable(true);
        rlv_BuyRef.setPullLoadEnable(false);
    }

    @Override
    public void onRefresh() {
        productMachineRequest();
    }

    @Override
    public void onLoadMore() {

    }

    private void productMachineRequest(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(BuyillMActivity.this,"user/queryCoinPros", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("list")){
                    productMachines = ProductMachine.initAttrWithJson(jsonObject.optJSONArray("list"),1);
                    Message message = new Message();
                    message.what = 2;
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
}
