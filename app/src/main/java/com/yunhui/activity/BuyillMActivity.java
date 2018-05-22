package com.yunhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.adapter.ProductMachineAdapter;
import com.yunhui.bean.ProductMachine;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.ToastUtil;

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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            int postion = (Integer) btn.getTag();
            Intent intent = new Intent(BuyillMActivity.this, BuyillMInfoActivity.class);
            intent.putExtra("millInfo",productMachines.get(postion));
            startActivityForResult(intent,8888);
        }
    };

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buymill);
        initView();
        ActivityQueueManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        productMachineRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    private void initView() {
        navigationBar.setTitle("购买云钻矿机");
        navigationBar.setBackground(R.color.color_4F5051);
        rlv_BuyRef = findViewById(R.id.buyref);
        productMachineAdapter = new ProductMachineAdapter(BuyillMActivity.this,productMachines,onClickListener);
        rlv_BuyRef.setAdapter(productMachineAdapter);
        rlv_BuyRef.setOnRefreshListViewListener(this);
        rlv_BuyRef.setPullRefreshEnable(false);
        rlv_BuyRef.setPullLoadEnable(false);
    }

    @Override
    public void onRefresh() {
        productMachineRequest();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 8888:
                BuyillMActivity.this.finish();
                break;
        }
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
